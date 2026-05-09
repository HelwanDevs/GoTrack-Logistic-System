package com.gotrack.inventory_service.filter;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

public class AuthenticationDetails {

    private String email;
    private String role;
    private String accountId;
    Authentication authentication;

    public AuthenticationDetails() {
        this.authentication = SecurityContextHolder.getContext().getAuthentication();
    }

    public String getEmail() {
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            User userDetails = (User) authentication.getPrincipal();
            this.email = userDetails.getUsername();
        }
        return email;
    }

    public String getRole() {
        if (authentication != null && authentication.getAuthorities() != null) {
            this.role = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .filter(auth -> auth.startsWith("ROLE_"))
                    .map(auth -> auth.substring(5))
                    .findFirst()
                    .orElse(null);
        }
        return role;
    }

    public String getAccountId() {
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            User userDetails = (User) authentication.getPrincipal();
            this.accountId = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .filter(auth -> auth.startsWith("ACCOUNT_"))
                    .map(auth -> auth.substring(8))
                    .findFirst()
                    .orElse(null);
        }
        return accountId;
    }
}
