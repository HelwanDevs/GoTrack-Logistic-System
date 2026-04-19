package com.gotrack.user_service.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProfileUpdateDTO {

    @NotBlank(message = "Validation errors")
    private String fullName;

    private String phoneNumber;
}