package com.gotrack.inventory_service.controller;

import com.gotrack.inventory_service.dto.ProductRequest;
import com.gotrack.inventory_service.dto.ReceiveItemsRequest;
import com.gotrack.inventory_service.model.InventoryItem;
import com.gotrack.inventory_service.model.Product;
import com.gotrack.inventory_service.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    // POST /api/inventory/products
    @PostMapping("/products")
    public ResponseEntity<?> defineProduct(@RequestBody ProductRequest request) {
        Product product = inventoryService.defineProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("productId", product.getId(), "message", "Product defined"));
    }

    // PUT /api/inventory/products/{id}
    @PutMapping("/products/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id,
                                           @RequestBody ProductRequest request) {
        inventoryService.updateProduct(id, request);
        return ResponseEntity.ok(Map.of("message", "Product updated"));
    }

    // POST /api/inventory/items/receive
    @PostMapping("/items/receive")
    public ResponseEntity<?> receiveItems(@RequestBody ReceiveItemsRequest request) {
        inventoryService.receiveItems(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Items received into inventory successfully"));
    }

    // GET /api/inventory/items
    @GetMapping("/items")
    public ResponseEntity<List<InventoryItem>> getAllItems() {
        return ResponseEntity.ok(inventoryService.getAllItems());
    }
}