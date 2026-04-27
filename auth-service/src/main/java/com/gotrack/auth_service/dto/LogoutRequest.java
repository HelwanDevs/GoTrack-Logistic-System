package com.gotrack.auth_service.dto;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
public class LogoutRequest {

    @NotBlank(message = "Refresh token is required")
    private String refreshToken;

}
