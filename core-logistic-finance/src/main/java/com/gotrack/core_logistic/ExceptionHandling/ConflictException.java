package com.gotrack.core_logistic.ExceptionHandling;



public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}