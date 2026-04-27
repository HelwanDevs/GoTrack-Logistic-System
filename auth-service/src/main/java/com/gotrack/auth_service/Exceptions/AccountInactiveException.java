package com.gotrack.auth_service.Exceptions;

public class AccountInactiveException extends RuntimeException {
    public AccountInactiveException(String message) {
        super(message);
    }

}
