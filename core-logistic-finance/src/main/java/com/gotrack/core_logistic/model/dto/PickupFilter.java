package com.gotrack.core_logistic.model.dto;

import lombok.Data;


@Data
public class PickupFilter {
    private Long id;
    private Long customerId;
    private Long courierId;
    private String status;
}