package com.gotrack.auth_service.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Role {
    ADMIN,
    EMPLOYEE,
    MERCHANT;
    @JsonCreator
    public static Role fromString(String value) {
        return Role.valueOf(value.toUpperCase());
    }
}
