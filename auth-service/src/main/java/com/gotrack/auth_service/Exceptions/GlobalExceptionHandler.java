package com.gotrack.auth_service.Exceptions;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.gotrack.auth_service.dto.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationErrors(MethodArgumentNotValidException ex) {
        String allErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        ErrorResponse errorBody = new ErrorResponse(
                400,
                "Bad Request",
                "Validation failed: " + allErrors);

        return ResponseEntity.status(400).body(errorBody);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex) {
        ErrorResponse errorBody = new ErrorResponse(
                401,
                "Unauthorized",
                "Incorrect email or password");
        return ResponseEntity.status(400).body(errorBody);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(AccountNotFoundException ex) {
        ErrorResponse errorBody = new ErrorResponse(
                404,
                "Not Found",
                ex.getMessage());
        return ResponseEntity.status(404).body(errorBody);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<?> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
        ErrorResponse errorBody = new ErrorResponse(
                409,
                "Conflict",
                ex.getMessage());
        return ResponseEntity.status(409).body(errorBody);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex) {
        ErrorResponse errorBody = new ErrorResponse(
                403,
                "Forbidden",
                ex.getMessage());
        return ResponseEntity.status(403).body(errorBody);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex) {
        ErrorResponse errorBody = new ErrorResponse(
                500,
                "Internal Server Error",
                "An unexpected internal error occurred");
        return ResponseEntity.status(500).body(errorBody);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<?> handleInvalidToken(InvalidTokenException ex) {
        ErrorResponse errorBody = new ErrorResponse(
                401,
                "Unauthorized",
                ex.getMessage());
        return ResponseEntity.status(401).body(errorBody);
    }

    @ExceptionHandler(RefreshTokenExpiredException.class)
    public ResponseEntity<?> handleExpired(RefreshTokenExpiredException ex) {
        ErrorResponse errorBody = new ErrorResponse(
                401,
                "Unauthorized",
                ex.getMessage());
        return ResponseEntity.status(401).body(errorBody);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<Object> handleDisabledException(DisabledException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", "This account is deactivated");
        body.put("status", HttpStatus.FORBIDDEN.value());

        return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Login Error");
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccountInactiveException.class)
    public ResponseEntity<Object> handleAccountInactiveException(AccountInactiveException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.FORBIDDEN.value());
        body.put("error", "Forbidden");
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
    }
}