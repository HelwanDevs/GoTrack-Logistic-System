package com.gotrack.inventory_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.gotrack.inventory_service.Dto.inventoryItemDto;
import com.gotrack.inventory_service.Service.InventoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/inventory/items")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService){
        this.inventoryService = inventoryService;
    }

     @PostMapping("/receive")
    public ResponseEntity<?> receiveItems(@Valid @RequestBody inventoryItemDto dto) {
        return ResponseEntity.status(201).body(
                inventoryService.receiveItems(dto)
        );
    }
    
}
