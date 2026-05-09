package com.gotrack.core_logistic.model.dto.Filters;

import lombok.Data;

@Data
public class PickupFilter {
    private Long id;
    private Long MERCHANTId;
    private Long courierId;
    private String status;
}