package com.gotrack.auth_service.dto;

import com.gotrack.auth_service.enums.Role;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListAccountsRequest {

    private Role role;

    @Email(message = "Invalid email format")
    private String email;

    private Boolean includeDeleted = false;

    private int page = 0;

    private int size = 10;

}
