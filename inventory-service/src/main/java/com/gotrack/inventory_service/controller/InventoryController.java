package com.gotrack.inventory_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gotrack.inventory_service.Dto.ApiResponse;
import com.gotrack.inventory_service.Dto.InventoryItemRequest;
import com.gotrack.inventory_service.Dto.InventoryItemResponse;
import com.gotrack.inventory_service.Dto.inventoryFilter;
import com.gotrack.inventory_service.Exception.ForbiddenException;
import com.gotrack.inventory_service.Service.InventoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/inventory/items")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;


    @PostMapping("/receive")
    public ResponseEntity<ApiResponse<Void>> receiveItems(
            @RequestHeader("role") String role,
            @Valid @RequestBody InventoryItemRequest dto) {

        if (!"ADMIN".equals(role) && !"EMPLOYEE".equals(role)) {
            throw new ForbiddenException("Unauthorized");
        }

        inventoryService.receiveItems(dto);

        return ResponseEntity.status(201).body(
            ApiResponse.<Void>builder()
                .status(201)
                .message("Items received into inventory successfully")
                .data(null)
                .build()
        );
}

    @GetMapping
    public ResponseEntity<ApiResponse<Page<InventoryItemResponse>>> getAllItems(
            @RequestHeader("role") String role,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        List<String> allowedRoles = List.of("ADMIN", "EMPLOYEE", "MERCHANT", "COURIER");
        if (!allowedRoles.contains(role)) {
            throw new ForbiddenException("Unauthorized");
        }

        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(
            ApiResponse.<Page<InventoryItemResponse>>builder()
                .status(200)
                .message("Items fetched successfully")
                .data(inventoryService.getAllItems(pageable))
                .build()
        );
    }


    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<InventoryItemResponse>>> getItems(
            @RequestHeader("role") String role,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            inventoryFilter filter){

        List<String> allowedRoles = List.of("ADMIN", "EMPLOYEE", "MERCHANT", "COURIER");
        if (!allowedRoles.contains(role)) {
            throw new ForbiddenException("Unauthorized");
        }

        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(
            ApiResponse.<Page<InventoryItemResponse>>builder()
                .status(200)
                .message("Items fetched successfully")
                .data(inventoryService.getItems(filter, pageable))
                .build()
        );
    
}  
}  
