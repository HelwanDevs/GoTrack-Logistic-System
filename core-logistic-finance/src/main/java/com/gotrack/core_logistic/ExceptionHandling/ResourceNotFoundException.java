package com.gotrack.core_logistic.ExceptionHandling;



public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
