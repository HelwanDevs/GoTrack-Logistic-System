package com.gotrack.user_branch_service.exceptions;

public class BranchNotFoundException extends RuntimeException{
    public BranchNotFoundException(String message) {
        super(message);
    }
}
