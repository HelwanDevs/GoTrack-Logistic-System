package com.gotrack.core_logistic.ExceptionHandling;

import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 404
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
        return buildResponse("NOT_FOUND", ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    //  409
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflict(ConflictException ex) {
        return buildResponse("CONFLICT", ex.getMessage(), HttpStatus.CONFLICT);
    }

    //  400 (Validation)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(err ->
                errors.put(err.getField(), err.getDefaultMessage())
        );

        return buildResponse("VALIDATION_ERROR", "Invalid input data", HttpStatus.BAD_REQUEST);
    }

    // 400 (Bad Request)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrity(DataIntegrityViolationException ex) {
        return buildResponse("CONFLICT", "Data conflict (e.g. duplicate email)", HttpStatus.CONFLICT);
    }

    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex) {
        return buildResponse("INTERNAL_ERROR", ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorResponse> buildResponse(
            String code,
            String message,
            HttpStatus status
    ) {
        ErrorResponse error = new ErrorResponse(
                code,
                message
        );

        return new ResponseEntity<>(error, status);
    }
}
