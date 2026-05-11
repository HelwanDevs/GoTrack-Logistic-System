package com.gotrack.inventory_service.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PickupResponceDTO {
    private Long id;
    private Long MERCHANTId;
    private Long courierId;
    private String status;
}