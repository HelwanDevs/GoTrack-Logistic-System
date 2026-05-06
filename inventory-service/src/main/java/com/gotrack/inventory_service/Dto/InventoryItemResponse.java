package com.gotrack.inventory_service.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class InventoryItemResponse {

    private Long id;
    private Long productId;
    private String productName;
    private Integer branchId;
    private String uniqueSku;
    private String status;
}