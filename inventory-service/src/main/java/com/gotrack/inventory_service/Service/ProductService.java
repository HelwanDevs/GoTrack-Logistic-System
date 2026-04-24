package com.gotrack.inventory_service.Service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.gotrack.inventory_service.Dto.ProductDTO;
import com.gotrack.inventory_service.Entity.Product;
import com.gotrack.inventory_service.Mapper.ProductMapper;
import com.gotrack.inventory_service.repository.ProductRepository;

import com.gotrack.inventory_service.Exception.ConflictException;
import com.gotrack.inventory_service.Exception.NotFoundException;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper; 

    public ProductService(ProductRepository productRepository, 
                          ProductMapper productMapper) { 
        this.productRepository = productRepository;
        this.productMapper = productMapper; 
    }

    public Map<String, Object> createProduct(ProductDTO productDto) {
    
    if (productRepository.existsByBaseSku(productDto.getBaseSku())) {
       throw new ConflictException("baseSku already exists");
    }

    Product product = productMapper.toEntity(productDto);
    Product saved = productRepository.save(product);

    Map<String, Object> response = new HashMap<>();
    response.put("productId", saved.getId());
    response.put("message", "Product defined");

    return response;
    }

    public Map<String, Object> updateProduct(Long id, ProductDTO dto) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                    "Product with ID " + id + " not found"));

        if (!product.getBaseSku().equals(dto.getBaseSku()) &&
    productRepository.existsByBaseSku(dto.getBaseSku())) {
    throw new ConflictException("baseSku already exists");
}

        product.setName(dto.getName());
        product.setBaseSku(dto.getBaseSku());

        productRepository.save(product);

     Map<String, Object> response = new HashMap<>();
    response.put("message", "Product updated");

    return response;
    }
}