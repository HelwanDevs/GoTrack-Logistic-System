package com.gotrack.inventory_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gotrack.inventory_service.Entity.InventoryItem;

public interface InventoryRepository extends JpaRepository<InventoryItem, Integer> {
    boolean existsByUniqueSku(String uniqueSku);
}