package com.gotrack.user_branch_service.service.imp;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gotrack.user_branch_service.domain.dto.ApiResponse;
import com.gotrack.user_branch_service.domain.dto.ProfileRequestDTO;
import com.gotrack.user_branch_service.domain.dto.ProfileResponseDTO;
import com.gotrack.user_branch_service.domain.dto.ProfileUpdateDTO;
import com.gotrack.user_branch_service.domain.entity.BranchEntity;
import com.gotrack.user_branch_service.domain.entity.ProfileEntity;
import com.gotrack.user_branch_service.domain.enums.ProfileStatus;
import com.gotrack.user_branch_service.domain.enums.ProfileType;
import com.gotrack.user_branch_service.domain.response.PageResponse;
import com.gotrack.user_branch_service.exceptions.ConflictException;
import com.gotrack.user_branch_service.exceptions.NotFoundException;
import com.gotrack.user_branch_service.filter.AuthenticationDetails;
import com.gotrack.user_branch_service.mappers.imp.ProfileMapperImp;
import com.gotrack.user_branch_service.repository.BranchRepository;
import com.gotrack.user_branch_service.repository.ProfileRepository;
import com.gotrack.user_branch_service.service.ProfileService;

import jakarta.persistence.criteria.Predicate;
import jakarta.ws.rs.ForbiddenException;

@Service
public class ProfileServiceImp implements ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ProfileMapperImp profileMapper;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private AccountServiceImp accountService;

    @Override
    public ApiResponse createProfile(ProfileRequestDTO dto) {

        // 409 — duplicate accountId check
        if (dto.getAccountId() != null &&
                profileRepository.findByAccountId(dto.getAccountId()).isPresent()) {
            throw new ConflictException("Account already linked to a profile");
        }

        // 409 — duplicate phone number check
        if (dto.getPhoneNumber() != null && !dto.getPhoneNumber().isBlank() &&
                profileRepository.findByPhoneNumber(dto.getPhoneNumber().trim()).isPresent()) {
            throw new ConflictException("Phone number already in use");
        }

        // 404 / 409 — validate branchId exists and is active
        BranchEntity branch = null;
        if (dto.getBranchId() != null) {
            branch = validateBranch(dto.getBranchId());
        }

        ProfileEntity entity = profileMapper.toEntity(dto);
        entity.setBranch(branch);

        AuthenticationDetails authDetails = new AuthenticationDetails();

        entity.setCreatedBy(authDetails.getAccountId());

        if (dto.getAccountId() != null) {
            if (!accountService.isAccountValid(dto.getAccountId())) {
                throw new NotFoundException("Account not found with ID: " + dto.getAccountId());
            }
            entity.setAccountId(dto.getAccountId());
        }

        ProfileEntity saved = profileRepository.save(entity);

        return new ApiResponse("Profile created", saved.getId());
    }

    @Override
    public ProfileResponseDTO findById(Long id) {
        AuthenticationDetails authDetails = new AuthenticationDetails();
        // check if MERCHANT is trying to access a profile that is not theirs
        if (authDetails.getRole().equals("MERCHANT")) {
            Long profileId = profileRepository.findByAccountId(authDetails.getAccountId())
                    .orElseThrow(() -> new NotFoundException(
                            "Profile not found for account ID: " + authDetails.getAccountId()))
                    .getId();
            if (!profileId.equals(id)) {
                throw new ForbiddenException("You are not authorized to view this profile");
            }
        }
        ProfileEntity entity = profileRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Profile not found"));
        return profileMapper.toDto(entity);
    }

    @Override
    public ApiResponse updateProfile(Long id, ProfileUpdateDTO dto) {

        // 404 — profile not found
        ProfileEntity entity = profileRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Profile not found"));
        AuthenticationDetails authDetails = new AuthenticationDetails();
        if (entity.getAccountId() != null && !entity.getAccountId().equals(authDetails.getAccountId())) {
            Boolean isSuperAdmin = accountService.isSuperAdmin(authDetails.getAccountId());
            // check if user is super admin to allow updating ADmIN and EMPLOYEE profiles,
            // otherwise only allow updating MERCHANT, COURIER, MERCHANT profiles
            // if user is ADMIN or EMPLOYEE
            if (List.of("ADMIN", "EMPLOYEE").contains(entity.getType().toString()) && !isSuperAdmin) {
                throw new ForbiddenException("You are not authorized to update this profile");
            } else if (List.of("MERCHANT", "COURIER", "MERCHANT").contains(entity.getType().toString()) &&
                    !List.of("ADMIN", "EMPLOYEE").contains(authDetails.getRole())) {
                throw new ForbiddenException("You are not authorized to update this profile");
            }
        }

        // FullName: updated when not null or blank
        if (dto.getFullName() != null && !dto.getFullName().isBlank()) {
            entity.setFullName(dto.getFullName().trim());
        }

        // PhoneNumber: updated when not null or blank, and must be unique (exclude
        // current profile)
        if (dto.getPhoneNumber() != null && !dto.getPhoneNumber().isBlank()) {
            profileRepository.findByPhoneNumber(dto.getPhoneNumber().trim())
                    .ifPresent(existing -> {
                        // 409 — duplicate phone number check (exclude current profile)

                        if (!existing.getId().equals(id)) {
                            throw new ConflictException("Phone number already in use");
                        }
                    });
            entity.setPhoneNumber(dto.getPhoneNumber().trim());
        }

        if (dto.getBranchId() != null) {
            BranchEntity branch = validateBranch(dto.getBranchId());
            entity.setBranch(branch);
        }
        if (dto.getAccountId() != null && !dto.getAccountId().isBlank()
                && !dto.getAccountId().equals(entity.getAccountId())) {
            if (profileRepository.findByAccountId(dto.getAccountId()).isPresent()) {
                throw new ConflictException("Account already linked to another profile");
            }
            if (!accountService.isAccountValid(dto.getAccountId())) {
                throw new NotFoundException("Account not found with ID: " + dto.getAccountId());
            }
            entity.setAccountId(dto.getAccountId());
        }
        if (dto.getBranchId() != null)
            entity.setBranch(validateBranch(dto.getBranchId()));
        if (dto.getStatus() != null)
            entity.setStatus(dto.getStatus());

        profileRepository.save(entity);

        return new ApiResponse("Profile updated successfully");
    }

    @Override
    public PageResponse<ProfileResponseDTO> getAllProfiles(Pageable pageable) {
        Page<ProfileEntity> page = profileRepository.findAll(pageable);

        List<ProfileResponseDTO> content = page.getContent()
                .stream()
                .map(profileMapper::toDto)
                .toList();
        return new PageResponse<>(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages());
    }

    @Override
    public PageResponse<ProfileResponseDTO> searchProfiles(
            String name, String phoneNumber, ProfileType type, Long branchId, ProfileStatus status, Pageable pageable) {

        Specification<ProfileEntity> spec = (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            // partial name search — case insensitive
            if (name != null && !name.isBlank()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("fullName")),
                        "%" + name.toLowerCase() + "%"));
            }

            // exact phone number match
            if (phoneNumber != null && !phoneNumber.isBlank()) {
                predicates.add(criteriaBuilder.equal(
                        root.get("phoneNumber"), phoneNumber));
            }

            // exact type match — EMPLOYEE, COURIER, MERCHANT, ADMIN
            if (type != null) {
                predicates.add(criteriaBuilder.equal(
                        root.get("type"), type));
            }

            // exact branchId match
            if (branchId != null) {
                predicates.add(criteriaBuilder.equal(
                        root.get("branch").get("id"), branchId));
            }

            // exact status match — ACTIVE, INACTIVE
            if (status != null) {
                predicates.add(criteriaBuilder.equal(
                        root.get("status"), status));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Page<ProfileEntity> page = profileRepository.findAll(spec, pageable);

        List<ProfileResponseDTO> content = page.getContent()
                .stream()
                .map(profileMapper::toDto)
                .toList();

        return new PageResponse<>(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages());

    }

    private BranchEntity validateBranch(Long branchId) {
        if (branchId == null)
            return null;

        BranchEntity branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new NotFoundException("Branch not found with ID: " + branchId));

        if (Boolean.TRUE.equals(branch.getIsDeleted())) {
            throw new ConflictException("Cannot assign profile to a deleted or inactive branch");
        }
        return branch;
    }

    @Override
    public ProfileResponseDTO getProfileByAccountId(String accountId) {
        if (accountId.startsWith("ACCOUNT_")) {
            accountId = accountId.substring(8);
        }
        ProfileEntity entity = profileRepository.findByAccountId(accountId)
                .orElseThrow(() -> new NotFoundException("Profile not found"));
        return profileMapper.toDto(entity);
    }


    @Override
    public ProfileResponseDTO linkProfileToAccount(String accountId, Long profileId) {
        if (accountId.startsWith("ACCOUNT_")) {
            accountId = accountId.substring(8);
        }
        ProfileEntity profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new NotFoundException("Profile not found with ID: " + profileId));
        profile.setAccountId(accountId);
        ProfileEntity savedProfile = profileRepository.save(profile);
        return profileMapper.toDto(savedProfile);
    }

}