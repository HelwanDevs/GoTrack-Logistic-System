package com.gotrack.core_logistic.model.dto.Filters;

import com.gotrack.core_logistic.enums.ShipmentStatus;

import lombok.Data;

@Data
public class ShipmentFilter {
    private Long id;
    private Long customerId;
    private Long courierId;
    private ShipmentStatus status;  
    private Long flyerNumber;
}