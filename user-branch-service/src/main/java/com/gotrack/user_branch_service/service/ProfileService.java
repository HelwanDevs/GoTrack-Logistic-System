package com.gotrack.user_branch_service.service;

import org.springframework.data.domain.Pageable;

import com.gotrack.user_branch_service.domain.dto.ApiResponse;
import com.gotrack.user_branch_service.domain.dto.ProfileRequestDTO;
import com.gotrack.user_branch_service.domain.dto.ProfileResponseDTO;
import com.gotrack.user_branch_service.domain.dto.ProfileUpdateDTO;
import com.gotrack.user_branch_service.domain.enums.ProfileStatus;
import com.gotrack.user_branch_service.domain.enums.ProfileType;
import com.gotrack.user_branch_service.domain.response.PageResponse;


public interface ProfileService {

    ApiResponse createProfile(ProfileRequestDTO dto);

    ApiResponse updateProfile(Long id, ProfileUpdateDTO dto);

    PageResponse<ProfileResponseDTO> getAllProfiles(Pageable pageable);
    ProfileResponseDTO findById(Long id);
    //Feature Addition: Add functionality to search for a profile by number, type, status, name, or BranchID
    PageResponse<ProfileResponseDTO> searchProfiles(
            String name, String phoneNumber, ProfileType type, Long branchId, ProfileStatus status, Pageable pageable);
}