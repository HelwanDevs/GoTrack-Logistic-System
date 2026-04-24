package com.gotrack.inventory_service.Dto;

import java.util.List;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter


public class inventoryItemDto {
    private int id;

    @NotNull(message = "Branch ID is required")
    private Integer branchId;

    @NotEmpty(message = "uniqueSkus must not be empty")
    private List<String> uniqueSkus;

    private String status;

    @NotNull(message = "productId is required")
    private Long productId; 

    private String productName;

    private Long pickupRequestId;
    
}
