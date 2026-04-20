package com.gotrack.inventory_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gotrack.inventory_service.Entity.Product;
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}