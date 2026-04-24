package com.gotrack.inventory_service.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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

   @Autowired
    private ProductService productService;


    @PostMapping
    public ResponseEntity<Map<String, Object>> createProduct(
            @RequestHeader("role") String role,
            @RequestHeader(value = "merchantId", required = false) Long merchantId,
            @Valid @RequestBody ProductDTO productDto) {

                // TODO: role and merchantId should be extracted from JWT token via API Gateway
               
        
        if (role.equals("MERCHANT") && merchantId != null) {
            if (!merchantId.equals(productDto.getMerchantId())) {
               throw new ForbiddenException("You can only create your own products");
            }
        }

        

        return ResponseEntity.status(201).body(
            productService.createProduct(productDto)
    );
    
}
}