package com.gotrack.inventory_service.controller;

import com.gotrack.inventory_service.model.Product;
import com.gotrack.inventory_service.repository.ProductRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    
    @GetMapping
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }


    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productRepository.save(product);
    }
}