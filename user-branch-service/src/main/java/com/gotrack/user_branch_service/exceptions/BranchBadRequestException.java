package com.gotrack.user_branch_service.exceptions;

public class BranchBadRequestException extends RuntimeException {
    public BranchBadRequestException(String message) {
        super(message);
    }
}
