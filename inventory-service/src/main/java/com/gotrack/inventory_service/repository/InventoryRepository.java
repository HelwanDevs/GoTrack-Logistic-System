package com.gotrack.inventory_service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import com.gotrack.inventory_service.Dto.InventoryItemResponse;
import com.gotrack.inventory_service.Entity.InventoryItem;

public interface InventoryRepository extends JpaRepository<InventoryItem, Integer> {
    boolean existsByUniqueSku(String uniqueSku);

    Page<InventoryItemResponse> findAll(Specification<InventoryItem> spec, Pageable pageable);
}