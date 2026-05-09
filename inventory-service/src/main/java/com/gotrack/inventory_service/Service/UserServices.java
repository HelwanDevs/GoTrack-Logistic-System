package com.gotrack.inventory_service.Service;

import org.springframework.beans.factory.annotation.Autowired;

import com.gotrack.inventory_service.Dto.ProfileResponseDTO;
import com.gotrack.inventory_service.client.feign.UserClient;
import org.springframework.stereotype.Service;

@Service
public class UserServices {
    @Autowired
    private UserClient userClient;

    public ProfileResponseDTO getProfileByAccountId(String accountId) {
        try {
            return userClient.getProfileByAccountId(accountId);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean ProfileExistById(Long id) {
        try {
            ProfileResponseDTO profile = userClient.getProfileById(id);
            return profile != null;
        } catch (Exception e) {
            return false;
        }
    }

    public ProfileResponseDTO get(Long id) {
        try {
            return userClient.getProfileById(id);
        } catch (Exception e) {
            return null;
        }
    }
}
