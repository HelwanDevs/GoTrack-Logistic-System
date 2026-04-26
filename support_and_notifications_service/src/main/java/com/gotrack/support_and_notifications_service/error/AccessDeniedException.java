package com.gotrack.support_and_notifications_service.error;

public class AccessDeniedException extends RuntimeException {

    public AccessDeniedException(String message) {
        super(message);
    }
    
}
