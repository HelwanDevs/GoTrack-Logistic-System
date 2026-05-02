package com.gotrack.user_service.service;

import com.gotrack.user_service.domain.dto.ProfileRequestDTO;
import com.gotrack.user_service.domain.dto.ProfileResponseDTO;
import com.gotrack.user_service.domain.dto.ProfileUpdateDTO;
import com.gotrack.user_service.domain.dto.ApiResponse;

import java.util.List;

public interface ProfileService {

    ApiResponse createProfile(ProfileRequestDTO dto);

    ApiResponse updateProfile(Long id, ProfileUpdateDTO dto);

    List<ProfileResponseDTO> getAllProfiles();

    //Feature Addition: Add functionality to search for a profile by number, name, or ID
    List<ProfileResponseDTO> searchProfiles(String name, String phoneNumber, Long id);
}