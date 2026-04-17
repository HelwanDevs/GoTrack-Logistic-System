package com.gotrack.inventory_service.dto;

import lombok.Data;

@Data
public class ProductRequest {
    private Long merchantId;
    private String name;
    private String baseSku;
}