package com.gotrack.inventory_service.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gotrack.inventory_service.Entity.Product;
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByBaseSku(String baseSku);

    Optional<Page<Product>> findByMerchantId(Long merchantId, Pageable pageable);
}