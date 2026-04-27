package com.gotrack.auth_service.dto;

import lombok.Getter;

@Getter
public class LogoutResponse {
    private String message;

    public LogoutResponse(String message) {
        this.message = message;
    }

}
