package com.gotrack.auth_service.dto;

public class AuthResponse {
    private String token;
    private String role;
    private String accountId;
    private String refreshToken;
    

    public AuthResponse(String token, String role, String accountId, String refreshToken) {
        this.token = token;
        this.role = role;
        this.accountId = accountId;
        this.refreshToken = refreshToken;
        
    }
    
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
    public String getRefreshToken() {
        return refreshToken;
    }
    
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public String getAccountId() {
        return accountId;
    }
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
