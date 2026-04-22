package com.gotrack.branch_service.exceptions;

public class BranchConflictException extends RuntimeException{
    public BranchConflictException(String message) {
        super(message);
    }
}
