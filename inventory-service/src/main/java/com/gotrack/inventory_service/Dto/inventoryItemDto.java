package com.gotrack.inventory_service.Dto;

import java.util.List;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class inventoryItemDto {
    private int id;

    @NotNull(message = "Branch ID is required")
    private int branchId;

    @NotEmpty(message = "uniqueSkus must not be empty")
    private List<String> uniqueSkus;

    private String status;

    @NotNull(message = "productId is required")
    private Long productId; 

    private String productName;

    private Long pickupRequestId;
    
}
