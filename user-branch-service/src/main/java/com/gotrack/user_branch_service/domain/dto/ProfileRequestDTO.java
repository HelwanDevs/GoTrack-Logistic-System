package com.gotrack.user_branch_service.domain.dto;

import com.gotrack.user_branch_service.domain.enums.ProfileType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProfileRequestDTO {

    @NotNull(message = "Full name is required")
    @NotBlank(message = "Full name is required and cannot be blank")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    private String fullName;

    @NotNull(message = "Phone number is required")
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^01[0125][0-9]{8}$", message = "Invalid Egyptian phone number format")
    private String phoneNumber;

    @NotNull(message = "Profile type must be specified")
    private ProfileType type;

    @Positive(message = "Account ID must be a positive number")
    private Long accountId; // Nullable, Couriers have no account

    @Positive(message = "Branch ID must be a positive number")
    private Long branchId;
    //TODO: validate that branchId exists and is active when creating/updating a profile via branch service 
    //TODO: validate that accountId exists and is active when creating/updating a profile (if not null) via auth service
}