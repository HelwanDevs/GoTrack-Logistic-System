package com.gotrack.user_service.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ProfileUpdateDTO {

    @NotBlank(message = "Validation errors")
    private String fullName;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^01[0125][0-9]{8}$", message = "Invalid Egyptian phone number format")
    
    private String phoneNumber;
}