package com.gotrack.inventory_service.Exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalalExceptionHandler {
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> MethodArgumentNotValidException(MethodArgumentNotValidException e){
        Map<String, String> erroMap = new HashMap<>();

        e.getBindingResult().getAllErrors().forEach(error->{
            String fieldName = ((FieldError)error).getField();
            String message = error.getDefaultMessage();
            erroMap.put(fieldName, message);
        });
        return erroMap;
    }

     @ExceptionHandler(ConflictException.class)
     @ResponseStatus(HttpStatus.CONFLICT)
     public Map<String, Object> handleConflict(ConflictException e) {
        Map<String, Object> error = new HashMap<>();
        error.put("status", 409);
        error.put("error", "Conflict");
        error.put("message", e.getMessage());
        return error;
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Map<String, Object> handleForbidden(ForbiddenException ex) {
        Map<String, Object> error = new HashMap<>();
    error.put("status", 403);
    error.put("error", "Forbidden");
    error.put("message", ex.getMessage());
    return error;
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handleNotFound(NotFoundException ex) {
        Map<String, Object> error = new HashMap<>();
    error.put("status", 404);
    error.put("error", "Not Found");
    error.put("message", ex.getMessage());
    return error;
}
}
