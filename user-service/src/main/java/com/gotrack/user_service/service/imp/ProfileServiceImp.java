package com.gotrack.user_service.service.imp;

import com.gotrack.user_service.domain.dto.ProfileRequestDTO;
import com.gotrack.user_service.domain.dto.ProfileResponseDTO;
import com.gotrack.user_service.domain.dto.ProfileUpdateDTO;
import com.gotrack.user_service.domain.entity.ProfileEntity;
import com.gotrack.user_service.exception.ConflictException;
import com.gotrack.user_service.exception.NotFoundException;
import com.gotrack.user_service.mappers.imp.ProfileMapper;
import com.gotrack.user_service.repository.ProfileRepository;
import com.gotrack.user_service.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gotrack.user_service.domain.dto.ApiResponse;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import java.util.ArrayList;

import java.util.List;

@Service
public class ProfileServiceImp implements ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ProfileMapper profileMapper;

    @Override
    public ApiResponse createProfile(ProfileRequestDTO dto) {

        // 409 — duplicate accountId check
        if (dto.getAccountId() != null &&
                profileRepository.findByAccountId(dto.getAccountId()).isPresent()) {
            throw new ConflictException("Profile already exists");
        }

        // 409 — duplicate phone number check
        if (profileRepository.findByPhoneNumber(dto.getPhoneNumber()).isPresent()) {
            throw new ConflictException("Phone number already in use");
        }

        ProfileEntity entity = profileMapper.toEntity(dto);

        // TODO: extract actual admin ID from JWT token via auth sercive
        // entity.setCreatedBy(jwtUtil.extractAccountId(token));
        entity.setCreatedBy("ADMIN");

        // TODO: validate branchId exists via branch-service API
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

        // 409 — duplicate phone number check (exclude current profile)
        profileRepository.findByPhoneNumber(dto.getPhoneNumber())
            .ifPresent(existing -> {
                if (!existing.getId().equals(id)) {
                    throw new ConflictException("Phone number already in use");
                }
            });

        profileMapper.updateEntity(dto, entity);
        profileRepository.save(entity);

        return new ApiResponse("Profile updated successfully");
    }

    @Override
    public List<ProfileResponseDTO> getAllProfiles() {
        // TODO: restrict to ADMIN and EMPLOYEE roles
        return profileRepository.findAll()
            .stream()
            .map(profileMapper::toDto)
            .toList();
    }

@Override
public List<ProfileResponseDTO> searchProfiles(String name, String phoneNumber, Long id) {
    // TODO: restrict to ADMIN and EMPLOYEE roles once JWT is ready

    Specification<ProfileEntity> spec = (root, query, criteriaBuilder) -> {

        List<Predicate> predicates = new ArrayList<>();

        // partial name search — case insensitive
        if (name != null && !name.isEmpty()) {
            predicates.add(criteriaBuilder.like(
                criteriaBuilder.lower(root.get("fullName")),
                "%" + name.toLowerCase() + "%"
            ));
        }

        // exact phone number match
        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            predicates.add(criteriaBuilder.equal(
                root.get("phoneNumber"), phoneNumber
            ));
        }

        // exact id match
        if (id != null) {
            predicates.add(criteriaBuilder.equal(
                root.get("id"), id
            ));
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    };

    return profileRepository.findAll(spec)
        .stream()
        .map(profileMapper::toDto)
        .toList();
}


}