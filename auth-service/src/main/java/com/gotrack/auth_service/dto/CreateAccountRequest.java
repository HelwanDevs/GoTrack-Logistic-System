package com.gotrack.auth_service.dto;

import com.gotrack.auth_service.enums.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;


@Getter
public class CreateAccountRequest {
    @Email(message = "Invalid email format should be like example@domain.com")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotNull(message = "Role is required")
    private Role role;

    @NotNull(message = "Profile ID is required")
    @Positive(message = "Profile ID must be a positive number")
    private Long profileId;

}
