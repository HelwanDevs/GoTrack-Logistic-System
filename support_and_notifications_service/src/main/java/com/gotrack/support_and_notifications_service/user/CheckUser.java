package com.gotrack.support_and_notifications_service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gotrack.support_and_notifications_service.error.ResourceNotFoundException;

@Service
public class CheckUser {

    @Autowired
    UserRepo repo;

    public void checkUserExists(String profileId) {
        if (!repo.existsById(profileId)) {
            throw new ResourceNotFoundException("User with ID " + profileId + " does not exist.");
        }

    }

    public String getUserEmailById(String profileId) {

        checkUserExists(profileId);
        return repo.getEmailByProfileId(profileId);

    }
}
