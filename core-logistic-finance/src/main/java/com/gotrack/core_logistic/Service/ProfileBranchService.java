package com.gotrack.core_logistic.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gotrack.core_logistic.client.feign.ProfileBranchClient;
import com.gotrack.core_logistic.model.dto.ProfileResponse;

@Service
public class ProfileBranchService {
    
    @Autowired
    private ProfileBranchClient profileBranchClient;


     public ProfileResponse getProfileByAccountId(String accountId) {
        try {
            return profileBranchClient.getProfileByAccountId(accountId);
        } catch (Exception e) {
            throw e;
        }
    }

    public ProfileResponse getProfileById(Long id) {
        return profileBranchClient.getProfileById(id);
    }
}
