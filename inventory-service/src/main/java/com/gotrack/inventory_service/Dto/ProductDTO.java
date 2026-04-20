package com.gotrack.inventory_service.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class ProductDTO {
    private Long id;
    private String name;
    private Long merchantId;
    private String baseSku;
    
}
