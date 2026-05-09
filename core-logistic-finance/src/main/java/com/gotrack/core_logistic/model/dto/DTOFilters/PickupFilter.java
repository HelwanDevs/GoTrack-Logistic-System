package com.gotrack.core_logistic.model.dto.DTOFilters;

import lombok.Data;


@Data
public class PickupFilter {
    private Long id;
    private Long customerId;
    private Long courierId;
    private String status;
}