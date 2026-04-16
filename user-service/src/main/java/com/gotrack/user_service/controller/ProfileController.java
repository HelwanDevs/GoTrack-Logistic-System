package com.gotrack.user_service.controller;

import com.gotrack.user_service.model.Profile;
import com.gotrack.user_service.service.ProfileService;
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
    public ResponseEntity<?> createProfile(@RequestBody Profile profile) {
        return ResponseEntity.status(201)
                .body(profileService.createProfile(profile));
    }

    @GetMapping 
    public List<Profile> getAllProfiles() {
        return profileService.getAllProfiles();
    }
}