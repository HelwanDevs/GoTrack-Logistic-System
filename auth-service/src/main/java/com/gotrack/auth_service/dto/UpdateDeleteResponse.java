package com.gotrack.auth_service.dto;

public class UpdateDeleteResponse {
    private String message;
    private String token;

    public UpdateDeleteResponse(String message , String token) {
        this.message = message;
        this.token = token;
    }
    public UpdateDeleteResponse(String message) {
        this.message = message;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    
    }
}