package com.gotrack.inventory_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.gotrack.inventory_service.Dto.inventoryItemDto;
import com.gotrack.inventory_service.Entity.InventoryItem;
import com.gotrack.inventory_service.Exception.ForbiddenException;
import com.gotrack.inventory_service.Service.InventoryService;

import java.util.List;
import java.util.Map;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/inventory/items")
public class InventoryController {


    @Autowired
private InventoryService inventoryService;

     @PostMapping("/receive")
    public ResponseEntity<Map<String, String>> receiveItems(@Valid @RequestBody inventoryItemDto dto) {
        return ResponseEntity.status(201).body(
                inventoryService.receiveItems(dto)
        );
    }

    @GetMapping
public ResponseEntity<List<InventoryItem>> getAllItems(
        @RequestHeader("role") String role) {

    // TODO: role should be extracted from JWT token via API Gateway
    List<String> allowedRoles = List.of("ADMIN", "EMPLOYEE", "MERCHANT", "COURIER");
    if (!allowedRoles.contains(role)) {
        throw new ForbiddenException("Unauthorized");
    }

    return ResponseEntity.status(200).body(
            inventoryService.getAllItems()
    );
}
    
}
