package com.gotrack.user_branch_service.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class BranchGlobalExceptionHandler {

    //Validation Handling (400)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String , Object>> handleValidation(MethodArgumentNotValidException ex){
        Map<String, Object> response = new HashMap<>();
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        response.put("status", 400);
        response.put("error", "Bad Request");
        response.put("message", "Validation failed");
        response.put("errors", errors);

        return ResponseEntity.badRequest().body(response);
    }


    @ExceptionHandler(BranchBadRequestException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequest(BranchBadRequestException ex) {

        Map<String, Object> response = new HashMap<>();
        response.put("status", 400);
        response.put("error", "Bad Request");
        response.put("message", ex.getMessage());

        return ResponseEntity.status(400).body(response);
    }

    @ExceptionHandler(BranchNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(BranchNotFoundException ex) {

        Map<String, Object> response = new HashMap<>();
        response.put("status", 404);
        response.put("error", "Not Found");
        response.put("message", ex.getMessage());

        return ResponseEntity.status(404).body(response);
    }

    @ExceptionHandler(BranchConflictException.class)
    public ResponseEntity<Map<String, Object>> handleConflict(BranchConflictException ex) {

        Map<String, Object> response = new HashMap<>();
        response.put("status", 409);
        response.put("error", "Conflict");
        response.put("message", ex.getMessage());

        return ResponseEntity.status(409).body(response);
    }



    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneral() {

        Map<String, Object> response = new HashMap<>();
        response.put("status", 500);
        response.put("error", "Internal Server Error");
        response.put("message", "Something went wrong");

        return ResponseEntity.status(500).body(response);
    }

}
