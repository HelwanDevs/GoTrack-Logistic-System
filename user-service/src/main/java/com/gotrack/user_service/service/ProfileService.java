package com.gotrack.user_service.service;

import com.gotrack.user_service.domain.dto.ProfileRequestDTO;
import com.gotrack.user_service.domain.dto.ProfileResponseDTO;
import com.gotrack.user_service.domain.dto.ProfileUpdateDTO;

import java.util.List;
import java.util.Map;

public interface ProfileService {

    Map<String, Object> createProfile(ProfileRequestDTO dto);

    Map<String, Object> updateProfile(Long id, ProfileUpdateDTO dto);

    List<ProfileResponseDTO> getAllProfiles();
}