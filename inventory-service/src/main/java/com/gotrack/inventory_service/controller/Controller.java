package com.gotrack.inventory_service.controller;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.gotrack.inventory_service.model.Product;
import com.gotrack.inventory_service.service.productsrvice;
@RestController
public class Controller {
 @Autowired
 private productsrvice productService;
 @GetMapping("index")
 public List<Product> list() {
 return productService.list();
 }
 @PostMapping("save")
 public Product save(@RequestBody Product product) {
 return productService.save(product);
 }
}