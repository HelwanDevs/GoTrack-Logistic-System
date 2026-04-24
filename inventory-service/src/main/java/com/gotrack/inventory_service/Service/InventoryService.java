package com.gotrack.inventory_service.Service;

import java.util.Map;
import org.springframework.stereotype.Service;

import com.gotrack.inventory_service.Dto.inventoryItemDto;
import com.gotrack.inventory_service.Entity.InventoryItem;
import com.gotrack.inventory_service.Entity.Product;
import com.gotrack.inventory_service.Entity.inventoryStatus;
import com.gotrack.inventory_service.Exception.ConflictException;
import com.gotrack.inventory_service.Exception.NotFoundException;
import com.gotrack.inventory_service.repository.InventoryRepository;
import com.gotrack.inventory_service.repository.ProductRepository;



@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;

    public InventoryService(InventoryRepository inventoryRepository,
                            ProductRepository productRepository) {
        this.inventoryRepository = inventoryRepository;
        this.productRepository = productRepository;
    }

    public Map<String, String> receiveItems(inventoryItemDto dto) {
       
        // TODO: validate branchId exists in database (requires BranchRepository from user-service)
        // Example: "Branch with ID " + dto.getBranchId() + " not found"
    Product product = productRepository.findById(dto.getProductId())
            .orElseThrow(() -> new NotFoundException( "Product with ID " + dto.getProductId() + " not found"));

    for (String sku : dto.getUniqueSkus()) {

        if (inventoryRepository.existsByUniqueSku(sku)) {
            throw new ConflictException("One or more Unique SKUs already exist in the inventory");
        }

        InventoryItem item = new InventoryItem();
        item.setProduct(product);
        item.setBranchId(dto.getBranchId());
        item.setUniqueSku(sku);
        item.setStatus(inventoryStatus.IN_STOCK);
        item.setPickupRequestId(dto.getPickupRequestId());

        inventoryRepository.save(item);
    }

    return Map.of("message", "Items received into inventory successfully");
}
}