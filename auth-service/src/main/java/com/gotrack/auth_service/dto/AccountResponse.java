package com.gotrack.auth_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class AccountResponse {
    private String id;
    private String email;
    private String role;
    private Boolean deleted;

}