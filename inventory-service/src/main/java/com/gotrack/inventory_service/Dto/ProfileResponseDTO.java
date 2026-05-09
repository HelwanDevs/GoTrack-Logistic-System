package com.gotrack.inventory_service.Dto;

import lombok.Data;
import java.time.LocalDateTime;

import com.gotrack.inventory_service.Enums.ProfileStatus;
import com.gotrack.inventory_service.Enums.ProfileType;

@Data
public class ProfileResponseDTO {

    private Long id;

    private String fullName;
    private String phoneNumber;

    private ProfileType type;

    private String accountId; // Nullable, Couriers have no account private BranchDTO branch;

    private ProfileStatus status;

    private LocalDateTime createdAt;
}