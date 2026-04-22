package com.gotrack.branch_service.exceptions;

public class BranchBadRequestException extends RuntimeException {
    public BranchBadRequestException(String message) {
        super(message);
    }
}
