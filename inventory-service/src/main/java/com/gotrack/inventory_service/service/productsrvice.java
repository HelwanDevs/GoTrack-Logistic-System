package com.gotrack.inventory_service.service;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gotrack.inventory_service.model.Product;
import com.gotrack.inventory_service.repository.productRepo;
@Service
public class productsrvice {
 @Autowired
 private productRepo productRepo;
 public List<Product> list() {
 return productRepo.findAll();
 }
 public Product save(Product product) {
 return productRepo.save(product);
 }
}