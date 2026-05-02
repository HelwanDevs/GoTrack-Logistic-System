package com.gotrack.user_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gotrack.user_service.domain.dto.ProfileRequestDTO;
import com.gotrack.user_service.domain.dto.ProfileResponseDTO;
import com.gotrack.user_service.domain.dto.ProfileUpdateDTO;
import com.gotrack.user_service.service.ProfileService;
import com.gotrack.user_service.domain.dto.ApiResponse;

import jakarta.validation.Valid;

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
    public ResponseEntity<ApiResponse> updateProfile(@PathVariable Long id,
            @RequestBody @Valid ProfileUpdateDTO dto) {
        return ResponseEntity.ok(profileService.updateProfile(id, dto));
    }

    //TODO: Restrict this endpoint to admin or employee users only
    @GetMapping
    public ResponseEntity<List<ProfileResponseDTO>> getAllProfiles() {
        return ResponseEntity.ok(profileService.getAllProfiles());
    }

    //TODO: Restrict this endpoint to admin or employee users only
    @GetMapping("/search")
    public ResponseEntity<List<ProfileResponseDTO>> searchProfiles(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) Long id) {
        return ResponseEntity.ok(profileService.searchProfiles(name, phoneNumber, id));
    }
}