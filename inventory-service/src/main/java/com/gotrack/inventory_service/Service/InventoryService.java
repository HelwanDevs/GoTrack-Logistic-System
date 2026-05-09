package com.gotrack.inventory_service.Service;

import java.util.ArrayList;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gotrack.inventory_service.Dto.InventoryItemRequest;
import com.gotrack.inventory_service.Dto.InventoryItemResponse;
import com.gotrack.inventory_service.Dto.PickupResponceDTO;
import com.gotrack.inventory_service.Dto.inventoryFilter;
import com.gotrack.inventory_service.Entity.InventoryItem;
import com.gotrack.inventory_service.Entity.Product;
import com.gotrack.inventory_service.Enums.InventoryStatus;
import com.gotrack.inventory_service.Exception.ConflictException;
import com.gotrack.inventory_service.Exception.NotFoundException;
import com.gotrack.inventory_service.Specifications.inventorySpecifications;
import com.gotrack.inventory_service.filter.AuthenticationDetails;
import com.gotrack.inventory_service.repository.InventoryRepository;
import com.gotrack.inventory_service.repository.ProductRepository;

@Service
public class InventoryService {
    private final PickupService pickupService;
    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;
    private final BranchServices branchServices;
    private final UserServices userServices;
    private final ProductService productService;

    public InventoryService(InventoryRepository inventoryRepository,
            ProductRepository productRepository, BranchServices branchServices, UserServices userServices,
            ProductService productService, PickupService pickupService) {
        this.inventoryRepository = inventoryRepository;
        this.productRepository = productRepository;
        this.branchServices = branchServices;
        this.userServices = userServices;
        this.productService = productService;
        this.pickupService = pickupService;
    }

    public void receiveItems(InventoryItemRequest dto) {

        Boolean branchExists = branchServices.getBranchById(dto.getBranchId()) != null;
        if (!branchExists) {
            throw new NotFoundException("No Branch with ID " + dto.getBranchId() + " was found");
        }
        Long productId = dto.getProductId();
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(
                        "Product with ID " + productId + " not found"));

        ArrayList<String> existingSkus = new ArrayList<String>();

        for (String sku : dto.getUniqueSkus()) {
            if (inventoryRepository.existsByUniqueSku(sku)) {
                existingSkus.add(sku);
            }
        }
        if (!existingSkus.isEmpty()) {
            throw new ConflictException(
                    "The following Unique SKUs already exist in the inventory: " + existingSkus.toString());
        }
        
        PickupResponceDTO pickup = pickupService.getPickup(dto.getPickupRequestId());

        if( pickup == null ||  !pickup.getStatus().equals("Completed") )
            throw new ConflictException("Pickup is not completed or does not exist");



        for (String sku : dto.getUniqueSkus()) {
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

        AuthenticationDetails authDetails = new AuthenticationDetails();
        if ("MERCHANT".equals(authDetails.getRole())) {
            Long merchantId = userServices.getProfileByAccountId(authDetails.getAccountId()).getId();
            if (merchantId == null) {
                throw new NotFoundException("You do not have a merchant profile");
            }
            if (filter.getMerchantId() != null && !filter.getMerchantId().equals(merchantId)) {
                throw new ConflictException("You can only filter by your own merchant ID");
            }
        }
        Specification<InventoryItem> spec = inventorySpecifications.filterInventory(filter);
        Page<InventoryItemResponse> inventoryFiltered = inventoryRepository.findAll(spec, pageable)
                .map(item -> InventoryItemResponse.builder()
                        .id(item.getId())
                        .productId(item.getProductId())
                        .productName(item.getProductName())
                        .branchId(item.getBranchId())
                        .uniqueSku(item.getUniqueSku())
                        .status(item.getStatus())
                        .build());
        if (!inventoryFiltered.isEmpty()) {
            inventoryFiltered.forEach(item -> {
                Long merchantId = productService.getMerchantIdByProductId(item.getProductId());
                item.setMerchantId(merchantId);
            });
        }
        return inventoryFiltered;
    }
}