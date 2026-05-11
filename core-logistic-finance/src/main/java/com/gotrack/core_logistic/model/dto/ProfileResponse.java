package com.gotrack.core_logistic.model.dto;

import java.time.LocalDateTime;

import com.gotrack.core_logistic.enums.ProfileStatus;
import com.gotrack.core_logistic.enums.ProfileType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileResponse {
      private Long id;

    private String fullName;
    private String phoneNumber;

    private ProfileType type;

    private String accountId; // Nullable, Couriers have no account 
    
    private BranchDTO branch;

    private ProfileStatus status;

    private LocalDateTime createdAt;
}
