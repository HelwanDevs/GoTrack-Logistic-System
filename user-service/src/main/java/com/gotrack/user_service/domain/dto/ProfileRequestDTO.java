package com.gotrack.user_service.domain.dto;

import com.gotrack.user_service.domain.entity.ProfileType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProfileRequestDTO {

    @NotBlank(message = "Missing mandatory fields")
    private String fullName;

    private String phoneNumber;

    @NotNull(message = "Missing mandatory fields")
    private ProfileType type;

    private Long accountId;  // Nullable, Couriers have no account

    private Long branchId;
}