package com.gotrack.support_and_notifications_service.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gotrack.support_and_notifications_service.client.dto.AccountDTO;
import com.gotrack.support_and_notifications_service.client.dto.ProfileDTO;
import com.gotrack.support_and_notifications_service.client.feign.UserProfileClient;
import com.gotrack.support_and_notifications_service.error.BadRequestException;
import com.gotrack.support_and_notifications_service.error.ResourceNotFoundException;
import com.gotrack.support_and_notifications_service.client.feign.AuthAccountClient;

@Service
public class CheckUser {

    private static final Logger log = LoggerFactory.getLogger(CheckUser.class);

    @Autowired
    private UserProfileClient userProfileClient;

    @Autowired
    private AuthAccountClient authAccountClient;

    public void checkUserExists(String profileId) {
        Long id = parseProfileId(profileId);
        ProfileDTO profile = userProfileClient.getProfile(id);
        if (profile == null || profile.getId() == null) {
            log.warn("[USER] Empty response for profileId={}", profileId);
            throw new ResourceNotFoundException("Profile " + profileId + " does not exist");
        }

    }

    public String getUserEmailById(String profileId) {
        Long id = parseProfileId(profileId);
        ProfileDTO profile = userProfileClient.getProfile(id);

        if (profile == null || profile.getId() == null) {
            log.warn("[USER] Empty response for profileId={}", profileId);
            throw new ResourceNotFoundException("Profile " + profileId + " does not exist");
        }

        if (profile.getAccountId() == null || profile.getAccountId().isBlank()) {
            log.warn("[USER] Profile {} has no linked account; cannot resolve email", profileId);
            throw new BadRequestException("Profile " + profileId + " has no linked account for email delivery");
        }

        AccountDTO account = authAccountClient.getAccount(profile.getAccountId());

        if (account.getEmail() == null || account.getEmail().isBlank()) {
            log.warn("[USER] Account {} has no email", profile.getAccountId());
            throw new ResourceNotFoundException("No email found for profile " + profileId);
        }

        return account.getEmail();
    }

    private static Long parseProfileId(String profileId) {
        if (profileId == null || profileId.isBlank()) {
            throw new BadRequestException("Profile id is required");
        }
        try {
            return Long.parseLong(profileId.trim());
        } catch (NumberFormatException ex) {
            throw new BadRequestException("Invalid profile id: " + profileId);
        }
    }
}
