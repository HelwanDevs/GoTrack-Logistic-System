package com.gotrack.auth_service.dto;

import com.gotrack.auth_service.enums.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public class UpdateAccountRequest {

    @Email(message = "Invalid email format")
    private String email;       

    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;    

    private Role role;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }          

}