package com.gotrack.user_branch_service.domain.dto;

import lombok.Data;
import java.time.LocalDateTime;

import com.gotrack.user_branch_service.domain.enums.ProfileStatus;
import com.gotrack.user_branch_service.domain.enums.ProfileType;

@Data
public class ProfileResponseDTO {

    private Long id;

    private String fullName;
    private String phoneNumber;

    private ProfileType type;

    private String accountId; // Nullable, Couriers have no account

    private BranchDTO branch;

    private ProfileStatus status;

    private LocalDateTime createdAt;
}