package com.gotrack.auth_service.dto;

import lombok.Data;
import java.time.LocalDateTime;

import com.gotrack.auth_service.enums.ProfileStatus;
import com.gotrack.auth_service.enums.ProfileType;

@Data
public class ProfileResponseDTO {

    private Long id;

    private String fullName;
    private String phoneNumber;

    private ProfileType type;

    private String accountId;

    private Long branchId;

    private ProfileStatus status;

    private LocalDateTime createdAt;
}