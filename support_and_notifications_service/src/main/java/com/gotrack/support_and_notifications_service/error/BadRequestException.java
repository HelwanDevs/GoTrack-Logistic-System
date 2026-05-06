package com.gotrack.support_and_notifications_service.error;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }

}
