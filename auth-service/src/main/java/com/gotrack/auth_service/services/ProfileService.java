package com.gotrack.auth_service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gotrack.auth_service.client.feign.ProfileClient;
import com.gotrack.auth_service.dto.ProfileResponseDTO;

@Service
public class ProfileService {
    @Autowired
    private ProfileClient profileClient;

    public ProfileResponseDTO getProfileById(Long id) {
        try {
            return profileClient.getProfileById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public ProfileResponseDTO linkProfileToAccount(String accountId, Long profileId) {
        try {
            return profileClient.linkProfileToAccount(accountId, profileId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
