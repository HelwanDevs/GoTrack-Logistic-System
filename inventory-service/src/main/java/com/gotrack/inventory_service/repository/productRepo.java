package com.gotrack.inventory_service.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gotrack.inventory_service.model.Product;
@Repository
public interface productRepo extends JpaRepository<Product, Long> {
}