package com.gotrack.user_branch_service.service.imp;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.gotrack.user_branch_service.exception.ConflictException;
import com.gotrack.user_branch_service.exception.NotFoundException;
import com.gotrack.user_branch_service.mappers.imp.ProfileMapperImp;
import com.gotrack.user_branch_service.repository.BranchRepository;
import com.gotrack.user_branch_service.repository.ProfileRepository;
import com.gotrack.user_branch_service.service.ProfileService;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import java.util.ArrayList;

import java.util.List;

@Service
public class ProfileServiceImp implements ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ProfileMapperImp profileMapper;

    @Autowired
    private BranchRepository branchRepository;

    @Override
    public ApiResponse createProfile(ProfileRequestDTO dto) {

        // 409 — duplicate accountId check
        if (dto.getAccountId() != null &&
                profileRepository.findByAccountId(dto.getAccountId()).isPresent()) {
            throw new ConflictException("Profile already exists");
        }

        // 409 — duplicate phone number check
        if (dto.getPhoneNumber() != null && !dto.getPhoneNumber().isBlank() &&
                profileRepository.findByPhoneNumber(dto.getPhoneNumber().trim()).isPresent()) {
            throw new ConflictException("Phone number already in use");
        }

        // 404 / 409 — validate branchId exists and is active
        validateBranch(dto.getBranchId());


        ProfileEntity entity = profileMapper.toEntity(dto);

        // TODO: extract actual admin ID from JWT token via auth sercive
        // entity.setCreatedBy(jwtUtil.extractAccountId(token));
        entity.setCreatedBy("ADMIN");

        // TODO: validate accountId exists via auth-service API

        ProfileEntity saved = profileRepository.save(entity);

        return new ApiResponse("Profile created", saved.getId());
    }

    @Override
    public ApiResponse updateProfile(Long id, ProfileUpdateDTO dto) {

        // 404 — profile not found
        ProfileEntity entity = profileRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Profile not found"));

        // TODO: extract role and accountId from JWT
        // TODO: if role is SELF, check that token accountId matches entity.getAccountId()
        // TODO: if role is not ADMIN and not SELF, throw ForbiddenException

        // FullName: updated when not null or blank 
        if (dto.getFullName() != null && !dto.getFullName().isBlank()) {
            entity.setFullName(dto.getFullName().trim());
        }

        // PhoneNumber: updated when not null or blank, and must be unique (exclude current profile)
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
        validateBranch(dto.getBranchId()); 
        entity.setBranchId(dto.getBranchId());
    }
        if (dto.getAccountId() != null)
            entity.setAccountId(dto.getAccountId());
        if (dto.getBranchId() != null)
            entity.setBranchId(dto.getBranchId());
        if (dto.getStatus() != null)
            entity.setStatus(dto.getStatus());

        profileRepository.save(entity);

        return new ApiResponse("Profile updated successfully");
    }

    @Override
    public PageResponse<ProfileResponseDTO> getAllProfiles(Pageable pageable) {
        // TODO: restrict to ADMIN and EMPLOYEE roles
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
        // TODO: restrict to ADMIN and EMPLOYEE roles once JWT is ready

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

            // exact type match — EMPLOYEE, COURIER, CUSTOMER, ADMIN
            if (type != null) {
                predicates.add(criteriaBuilder.equal(
                        root.get("type"), type));
            }

            // exact branchId match
            if (branchId != null) {
                predicates.add(criteriaBuilder.equal(
                        root.get("branchId"), branchId));
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

    private void validateBranch(Long branchId) {
        if (branchId == null)
            return;

        BranchEntity branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new NotFoundException("Branch not found with ID: " + branchId));

        if (Boolean.TRUE.equals(branch.getIsDeleted())) {
            throw new ConflictException("Cannot assign profile to a deleted or inactive branch");
        }
    }
    
    
}