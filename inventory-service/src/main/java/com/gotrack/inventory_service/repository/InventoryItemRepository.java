package com.gotrack.inventory_service.repository;

import com.gotrack.inventory_service.model.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {
    boolean existsByUniqueSku(String uniqueSku);
}