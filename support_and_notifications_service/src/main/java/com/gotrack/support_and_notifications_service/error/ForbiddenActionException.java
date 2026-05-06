package com.gotrack.support_and_notifications_service.error;

public class ForbiddenActionException extends RuntimeException {
    public ForbiddenActionException(String message) {
        super(message);
    }

}
