package com.gotrack.user_branch_service.domain.dto;

import com.gotrack.user_branch_service.domain.enums.ProfileStatus;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ProfileUpdateDTO {

    private String fullName;

    @Pattern(regexp = "^01[0125][0-9]{8}$", message = "Invalid Egyptian phone number format")
    private String phoneNumber;

    private ProfileStatus status;

    @Positive(message = "Branch ID must be a positive number")
    private Long branchId; // Nullable

    @Positive(message = "Account ID must be a positive number")
    private Long accountId; // Nullable, Couriers have no account
}