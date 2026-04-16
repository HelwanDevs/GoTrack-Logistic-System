package com.gotrack.user_service.service;

import com.gotrack.user_service.model.Profile;
import com.gotrack.user_service.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ProfileService {

    @Autowired  
    private ProfileRepository profileRepository;

    public Map<String, Object> createProfile(Profile profile) {
        Profile saved = profileRepository.save(profile); 
        return Map.of(
            "profileId", saved.getId(),
            "message", "Profile created"
        );
    }

    public List<Profile> getAllProfiles() {
        return profileRepository.findAll(); 
    }

}