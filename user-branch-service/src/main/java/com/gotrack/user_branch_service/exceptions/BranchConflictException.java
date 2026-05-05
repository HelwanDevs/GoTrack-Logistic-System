package com.gotrack.user_branch_service.exceptions;

public class BranchConflictException extends RuntimeException{
    public BranchConflictException(String message) {
        super(message);
    }
}
