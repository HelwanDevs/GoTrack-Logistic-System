package com.gotrack.user_branch_service.exceptions;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.gotrack.user_branch_service.domain.dto.ApiResponse;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDeniedException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Validation Handling (400)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, Object> response = new HashMap<>();
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        response.put("status", 400);
        response.put("error", "Bad Request");
        response.put("message", "Validation failed");
        response.put("errors", errors);

        return ResponseEntity.badRequest().body(response);
    }

    // Bad request Handling (400)
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequest(BadRequestException ex) {

        Map<String, Object> response = new HashMap<>();
        response.put("status", 400);
        response.put("error", "Bad Request");
        response.put("message", ex.getMessage());

        return ResponseEntity.status(400).body(response);
    }

    // Not found (404)
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(NotFoundException ex) {

        Map<String, Object> response = new HashMap<>();
        response.put("status", 404);
        response.put("error", "Not Found");
        response.put("message", ex.getMessage());

        return ResponseEntity.status(404).body(response);
    }

    // Conflict Handling (409)
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<Map<String, Object>> handleConflict(ConflictException ex) {

        Map<String, Object> response = new HashMap<>();
        response.put("status", 409);
        response.put("error", "Conflict");
        response.put("message", ex.getMessage());

        return ResponseEntity.status(409).body(response);
    }

    // 400 — @Positive @PathVariable / @RequestParam validation
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolation(ConstraintViolationException ex) {

        Map<String, Object> response = new HashMap<>();
        String message = ex.getConstraintViolations()
                .iterator()
                .next()
                .getMessage();

        response.put("status", 400);
        response.put("error", "Bad Request");
        response.put("message", message);

        return ResponseEntity.status(400).body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
        // log.warn("Invalid request format: {}", e.getMessage());

        String message = "Invalid request format";
        // Check if it's a JSON parsing error for type mismatch
        if (e.getCause() != null && e.getCause().getMessage() != null) {
            String cause = e.getCause().getMessage();
            if (cause.contains("Cannot deserialize value of type")) {
                message = "Invalid data type in request. Please check your field types.";
            }
        }

        ApiResponse response = new ApiResponse(message, null);
        return ResponseEntity.badRequest().body(response);
    }

    // Path variable type mismatch (400)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex) {
        Map<String, Object> response = new HashMap<>();
        String fieldName = ex.getName();
        String requiredType = ex.getRequiredType().getSimpleName();
        String providedValue = ex.getValue() != null ? ex.getValue().toString() : "null";

        response.put("status", 400);
        response.put("error", "Bad Request");
        response.put("message",
                "Invalid data type for '" + fieldName + "'. Expected " + requiredType + " but got: " + providedValue);

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler({ AuthorizationDeniedException.class, AccessDeniedException.class })
    public ResponseEntity<Map<String, Object>> handleAccessDenied(Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", 403);
        response.put("error", "Forbidden");
        response.put("message", "User lacks necessary permissions / role to access this Api");
        return ResponseEntity.status(403).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGenericException(Exception e) {
        // log.error("Unexpected error", e);
        ApiResponse response = new ApiResponse("An unexpected error occurred", null);
        return ResponseEntity.status(500).body(response);
    }

}
