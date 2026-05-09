package com.gotrack.inventory_service.Exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestControllerAdvice
public class GlobalalFeignExceptionHandler {

    
    @ExceptionHandler(feign.FeignException.class)
    public ResponseEntity<Map<String, Object>> handleFeignException(feign.FeignException e) {
        Map<String, Object> error = new HashMap<>();
        
        int status = e.status() > 0 ? e.status() : 500;
        
        error.put("status", status);
        error.put("error", "External Service Error");
        
        String cleanMessage = "An error occurred while calling an external service";
        try {
            if (e.contentUTF8() != null && !e.contentUTF8().isEmpty()) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonNode = mapper.readTree(e.contentUTF8());
                if (jsonNode.has("message")) {
                    cleanMessage = jsonNode.get("message").asText();
                } else {
                    cleanMessage = e.contentUTF8();
                }
            }
        } catch (Exception ex) {
            // Unparseable JSON, fallback to FeignException message
            cleanMessage = e.getMessage();
        }

        error.put("message", cleanMessage);

        return ResponseEntity.status(status).body(error);
    }
}