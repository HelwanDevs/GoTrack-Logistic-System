package com.gotrack.user_branch_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gotrack.user_branch_service.domain.dto.ApiResponse;
import com.gotrack.user_branch_service.domain.dto.ProfileRequestDTO;
import com.gotrack.user_branch_service.domain.dto.ProfileResponseDTO;
import com.gotrack.user_branch_service.domain.dto.ProfileUpdateDTO;
import com.gotrack.user_branch_service.domain.enums.ProfileStatus;
import com.gotrack.user_branch_service.domain.enums.ProfileType;
import com.gotrack.user_branch_service.domain.response.PageResponse;
import com.gotrack.user_branch_service.service.ProfileService;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/users/profiles")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    //TODO: Restrict this endpoint to admin users only
    @PostMapping
    public ResponseEntity<ApiResponse> createProfile(@RequestBody @Valid ProfileRequestDTO dto) {
        return ResponseEntity.status(201)
                .body(profileService.createProfile(dto));
    }

    //TODO: Restrict this endpoint to admin or the profile owner only
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateProfile(
            @PathVariable @Positive(message = "ID must be a positive number") Long id,
            @RequestBody @Valid ProfileUpdateDTO dto) {
        return ResponseEntity.ok(profileService.updateProfile(id, dto));
    }

    //TODO: Restrict this endpoint to admin or employee users only
@GetMapping
public ResponseEntity<PageResponse<ProfileResponseDTO>> getAllProfiles(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "fullName") String sortBy) {

    Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
    return ResponseEntity.ok(profileService.getAllProfiles(pageable));
}

    //TODO: Restrict this endpoint to admin or employee users only
@GetMapping("/search")
public ResponseEntity<PageResponse<ProfileResponseDTO>> searchProfiles(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String phoneNumber,
        @RequestParam(required = false) ProfileType type,
        @RequestParam(required = false) Long branchId,
        @RequestParam(required = false) ProfileStatus status,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "fullName") String sortBy) {

    Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
    return ResponseEntity.ok(
        profileService.searchProfiles(name, phoneNumber, type, branchId, status, pageable)
    );
}
}