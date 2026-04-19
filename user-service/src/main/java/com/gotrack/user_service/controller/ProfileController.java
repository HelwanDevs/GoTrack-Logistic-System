package com.gotrack.user_service.controller;

import com.gotrack.user_service.domain.dto.ProfileRequestDTO;
import com.gotrack.user_service.domain.dto.ProfileResponseDTO;
import com.gotrack.user_service.domain.dto.ProfileUpdateDTO;
import com.gotrack.user_service.service.ProfileService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/profiles")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @PostMapping
    public ResponseEntity<?> createProfile(@RequestBody @Valid ProfileRequestDTO dto) {
        return ResponseEntity.status(201)
                .body(profileService.createProfile(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProfile(@PathVariable Long id,
                                           @RequestBody @Valid ProfileUpdateDTO dto) {
        return ResponseEntity.ok(profileService.updateProfile(id, dto));
    }

    @GetMapping
    public List<ProfileResponseDTO> getAllProfiles() {
        return profileService.getAllProfiles();
    }
}