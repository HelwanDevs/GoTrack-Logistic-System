package com.gotrack.inventory_service.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class inventoryItemDto {
    private int id;
    private int branchId;
    private String uniqueSku;
    private String status;
    private Long productId; 
    private String productName;
    
}
