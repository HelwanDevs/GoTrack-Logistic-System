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

import java.util.List;
import java.util.Map;

@Service
public class ProfileServiceImp implements ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ProfileMapper profileMapper;

    @Override
    public Map<String, Object> createProfile(ProfileRequestDTO dto) {

        if (dto.getAccountId() != null &&
                profileRepository.findByAccountId(dto.getAccountId()).isPresent()) {
            throw new ConflictException("Profile already exists");
        }

        ProfileEntity entity = profileMapper.toEntity(dto);
        entity.setCreatedBy("ADMIN");
        ProfileEntity saved = profileRepository.save(entity);

        return Map.of(
            "profileId", saved.getId(),
            "message", "Profile created"
        );
    }

    @Override
    public Map<String, Object> updateProfile(Long id, ProfileUpdateDTO dto) {

        ProfileEntity entity = profileRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Profile not found"));


        profileMapper.updateEntity(dto, entity);
        profileRepository.save(entity);

        return Map.of("message", "Profile updated successfully");
    }

    @Override
    public List<ProfileResponseDTO> getAllProfiles() {
        return profileRepository.findAll()
            .stream()
            .map(profileMapper::toDto)
            .toList();
    }
}