package com.gotrack.inventory_service.service;

import com.gotrack.inventory_service.dto.ProductRequest;
import com.gotrack.inventory_service.dto.ReceiveItemsRequest;
import com.gotrack.inventory_service.model.InventoryItem;
import com.gotrack.inventory_service.model.Product;
import com.gotrack.inventory_service.repository.InventoryItemRepository;
import com.gotrack.inventory_service.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class InventoryService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventoryItemRepository inventoryItemRepository;

    // POST /api/inventory/products
    public Product defineProduct(ProductRequest request) {
        if (productRepository.existsByBaseSku(request.getBaseSku())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "baseSku already exists");
        }

        Product product = new Product();
        product.setMerchantId(request.getMerchantId());
        product.setName(request.getName());
        product.setBaseSku(request.getBaseSku());

        return productRepository.save(product);
    }

    // PUT /api/inventory/products/{id}
    public Product updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product ID not found"));

        product.setName(request.getName());
        product.setBaseSku(request.getBaseSku());

        return productRepository.save(product);
    }

    // POST /api/inventory/items/receive
    public void receiveItems(ReceiveItemsRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product ID or Branch ID not found"));

        for (String sku : request.getUniqueSkus()) {
            if (inventoryItemRepository.existsByUniqueSku(sku)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "One or more Unique SKUs already exist in the inventory");
            }
        }

        for (String sku : request.getUniqueSkus()) {
            InventoryItem item = new InventoryItem();
            item.setProduct(product);
            item.setBranchId(request.getBranchId());
            item.setPickupRequestId(request.getPickupRequestId());
            item.setUniqueSku(sku);
            inventoryItemRepository.save(item);
        }
    }

    // GET /api/inventory/items
    public List<InventoryItem> getAllItems() {
        return inventoryItemRepository.findAll();
    }
}