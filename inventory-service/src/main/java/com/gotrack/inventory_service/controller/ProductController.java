package com.gotrack.inventory_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gotrack.inventory_service.Dto.ApiResponse;
import com.gotrack.inventory_service.Dto.ProductDTO;
import com.gotrack.inventory_service.Dto.ProductResponseDTO;
import com.gotrack.inventory_service.Exception.ForbiddenException;
import com.gotrack.inventory_service.Service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/inventory/products")
public class ProductController {

@Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponseDTO>> createProduct(
            @RequestHeader("role") String role,
            @RequestHeader(value = "merchantId", required = false) Long merchantId,
            @Valid @RequestBody ProductDTO productDto) {

        if ("MERCHANT".equals(role) && merchantId != null) {
            if (!merchantId.equals(productDto.getMerchantId())) {
                throw new ForbiddenException("You can only create your own products");
            }
        }

        ProductResponseDTO created = productService.createProduct(productDto);

        return ResponseEntity.status(201).body(
            ApiResponse.<ProductResponseDTO>builder()
                .status(201)
                .message("Product defined")
                .data(created)
                .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> updateProduct(
            @PathVariable Long id,
            @RequestHeader("role") String role,
            @Valid @RequestBody ProductDTO productDto) {

        if (!"ADMIN".equals(role) && !"EMPLOYEE".equals(role)) {
            throw new ForbiddenException("You are not allowed to update products");
        }

        productService.updateProduct(id, productDto);

        return ResponseEntity.ok(
            ApiResponse.<Void>builder()
                .status(200)
                .message("Product updated")
                .data(null)
                .build()
        );
    }
}