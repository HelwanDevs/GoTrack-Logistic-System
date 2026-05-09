package com.gotrack.inventory_service.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gotrack.inventory_service.Dto.InventoryItemRequest;
import com.gotrack.inventory_service.Dto.InventoryItemResponse;
import com.gotrack.inventory_service.Dto.inventoryFilter;
import com.gotrack.inventory_service.Entity.InventoryItem;
import com.gotrack.inventory_service.Entity.Product;
import com.gotrack.inventory_service.Enums.InventoryStatus;
import com.gotrack.inventory_service.Exception.ConflictException;
import com.gotrack.inventory_service.Exception.NotFoundException;
import com.gotrack.inventory_service.Specifications.inventorySpecifications;
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

    // TODO: Cross-service validation - verify branchId exists via user-service
    public void receiveItems(InventoryItemRequest dto) {

        Long productId = dto.getProductId();
        if (productId == null) {
            throw new NotFoundException("Product ID is required");
        }


        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(
                        "Product with ID " + productId + " not found"));

        for (String sku : dto.getUniqueSkus()) {
            if (inventoryRepository.existsByUniqueSku(sku)) {
                throw new ConflictException("One or more Unique SKUs already exist in the inventory");
            }

            InventoryItem item = new InventoryItem();
            item.setProduct(product);
            item.setBranchId(dto.getBranchId());
            item.setUniqueSku(sku);
            item.setStatus(InventoryStatus.IN_STOCK);
            item.setPickupRequestId(dto.getPickupRequestId());

            inventoryRepository.save(item);
        }
    }

    public Page<InventoryItemResponse> getAllItems(Pageable pageable) {
        if (pageable == null) {
            pageable = Pageable.unpaged();
        }
        return inventoryRepository.findAll(pageable)
                .map(item -> InventoryItemResponse.builder()
                        .id(item.getId())
                        .productId(item.getProduct().getId())
                        .productName(item.getProduct().getName())
                        .branchId(item.getBranchId())
                        .uniqueSku(item.getUniqueSku())
                        .status(item.getStatus().name())
                        .build());
    }

    public Page<InventoryItemResponse> getItems(inventoryFilter filter, Pageable pageable) {

        Specification<InventoryItem> spec = inventorySpecifications.filterInventory(filter);

        return inventoryRepository.findAll(spec, pageable)
                .map(item -> InventoryItemResponse.builder()
                        .id(item.getId())
                        .productId(item.getProductId())
                        .productName(item.getProductName())
                        .branchId(item.getBranchId())
                        .uniqueSku(item.getUniqueSku())
                        .status(item.getStatus())
                        .build());
    }
}