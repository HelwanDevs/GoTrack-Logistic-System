package com.gotrack.inventory_service.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gotrack.inventory_service.Dto.ProductDTO;
import com.gotrack.inventory_service.Service.ProductService;

import jakarta.validation.Valid;
import com.gotrack.inventory_service.Exception.ForbiddenException; 

@RestController
@RequestMapping("/api/inventory/products")
public class ProductController{

    private final ProductService productService;

    public ProductController(ProductService productService) {
    this.productService = productService;
}


    @PostMapping
    public ResponseEntity<?> createProduct(
            @RequestHeader("role") String role,
            @RequestHeader(value = "merchantId", required = false) Long merchantId,
            @Valid @RequestBody ProductDTO productDto) {

        
        if (!role.equals("ADMIN") && !role.equals("EMPLOYEE") && !role.equals("MERCHANT")) {
            throw new ForbiddenException("Unauthorized");
        }

        
        if (role.equals("MERCHANT") && merchantId != null) {
            if (!merchantId.equals(productDto.getMerchantId())) {
               throw new ForbiddenException("You can only create your own products");
            }
        }

        Map<String, Object> savedProduct = productService.createProduct(productDto);

        return ResponseEntity.status(201).body(savedProduct);
    }
}