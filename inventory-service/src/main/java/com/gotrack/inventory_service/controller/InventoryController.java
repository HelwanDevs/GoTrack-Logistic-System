package com.gotrack.inventory_service.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gotrack.inventory_service.Dto.ApiResponse;
import com.gotrack.inventory_service.Dto.InventoryItemRequest;
import com.gotrack.inventory_service.Dto.InventoryItemResponse;
import com.gotrack.inventory_service.Dto.inventoryFilter;
import com.gotrack.inventory_service.Service.InventoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/inventory/items")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @PostMapping("/receive")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<ApiResponse<Void>> receiveItems(
            @Valid @RequestBody InventoryItemRequest dto) {

        inventoryService.receiveItems(dto);

        return ResponseEntity.status(201).body(
                ApiResponse.<Void>builder()
                        .status(201)
                        .message("Items received into inventory successfully")
                        .data(null)
                        .build());
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE', 'MERCHANT')")
    public ResponseEntity<ApiResponse<Page<InventoryItemResponse>>> getAllItems(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(
                ApiResponse.<Page<InventoryItemResponse>>builder()
                        .status(200)
                        .message("Items fetched successfully")
                        .data(inventoryService.getAllItems(pageable))
                        .build());
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE', 'MERCHANT')")
    public ResponseEntity<ApiResponse<Page<InventoryItemResponse>>> getItems(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            inventoryFilter filter) {

        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(
                ApiResponse.<Page<InventoryItemResponse>>builder()
                        .status(200)
                        .message("Items fetched successfully")
                        .data(inventoryService.getItems(filter, pageable))
                        .build());

    }
}
