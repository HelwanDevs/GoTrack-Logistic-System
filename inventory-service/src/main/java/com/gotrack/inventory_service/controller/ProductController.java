package com.gotrack.inventory_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gotrack.inventory_service.Dto.ApiResponse;
import com.gotrack.inventory_service.Dto.ProductDTO;
import com.gotrack.inventory_service.Dto.ProductResponseDTO;
import com.gotrack.inventory_service.Dto.UpdateProductDTO;
import com.gotrack.inventory_service.Exception.ForbiddenException;
import com.gotrack.inventory_service.Service.ProductService;
import com.gotrack.inventory_service.Service.UserServices;
import com.gotrack.inventory_service.filter.AuthenticationDetails;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/inventory/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserServices userServices;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE', 'MERCHANT')")
    public ResponseEntity<ApiResponse<ProductResponseDTO>> createProduct(
            @Valid @RequestBody ProductDTO productDto) {

        ProductResponseDTO created = productService.createProduct(productDto);

        return ResponseEntity.status(201).body(
                ApiResponse.<ProductResponseDTO>builder()
                        .status(201)
                        .message("Product defined")
                        .data(created)
                        .build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE' , 'MERCHANT')")
    public ResponseEntity<ApiResponse<Void>> updateProduct(
            @Valid @RequestBody UpdateProductDTO productDto) {
        productService.updateProduct(productDto);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .status(200)
                        .message("Product updated")
                        .data(null)
                        .build());
    }

    @GetMapping("/merchent/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<ApiResponse<Page<ProductResponseDTO>>> getProductsByMerchantId(
            @PathVariable Long id, 
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ProductResponseDTO> products = productService.getProducts(id, pageable);
        return ResponseEntity.ok(
                ApiResponse.<Page<ProductResponseDTO>>builder()
                        .status(200)
                        .message("Products found")
                        .data(products)
                        .build());
    }

    @GetMapping("/myProducts")
    @PreAuthorize("hasAnyRole( 'MERCHANT')")
    public ResponseEntity<ApiResponse<Page<ProductResponseDTO>>> getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);

        AuthenticationDetails authDetails = new AuthenticationDetails();
        Long merchantId = userServices.getProfileByAccountId(authDetails.getAccountId()).getId();
        if (merchantId == null) {
            throw new ForbiddenException("You do not have a merchant profile");
        }
        Page<ProductResponseDTO> products = productService.getProducts(merchantId, pageable);

        return ResponseEntity.ok(
                ApiResponse.<Page<ProductResponseDTO>>builder()
                        .status(200)
                        .message("Product found")
                        .data(products)
                        .build());
    }
}