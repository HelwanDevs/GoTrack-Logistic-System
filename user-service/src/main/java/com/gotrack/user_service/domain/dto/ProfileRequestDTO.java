package com.gotrack.user_service.domain.dto;

import com.gotrack.user_service.domain.entity.ProfileType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ProfileRequestDTO {

    @NotBlank(message = "Missing mandatory fields")
    private String fullName;

    @NotNull(message = "Phone number is required")
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^01[0125][0-9]{8}$", message = "Invalid Egyptian phone number format")
    private String phoneNumber;

    @NotNull(message = "Missing mandatory fields")
    private ProfileType type;

    private Long accountId;  // Nullable, Couriers have no account

    private Long branchId;
    //TODO: validate that branchId exists and is active when creating/updating a profile via branch service 
    //TODO: validate that accountId exists and is active when creating/updating a profile (if not null) via auth service
}