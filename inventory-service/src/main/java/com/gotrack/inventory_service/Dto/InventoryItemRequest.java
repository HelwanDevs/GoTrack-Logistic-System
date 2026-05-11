package com.gotrack.inventory_service.Dto;

import java.util.List;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryItemRequest {

    @NotNull(message = "productId is required")
    @Positive(message = "productId must be a positive number")
    private Long productId;

    @NotNull(message = "branchId is required")
    @Min(value = 1, message = "Branch ID must be a positive number")
    private Long branchId;

    @NotNull(message = "pickupRequestId is required")
    @Positive(message = "pickupRequestId must be a positive number")
    private Long pickupRequestId;


    @NotEmpty(message = "uniqueSku is required")
    private List<@NotBlank(message = "Each SKU must not be blank") String> uniqueSkus;
}