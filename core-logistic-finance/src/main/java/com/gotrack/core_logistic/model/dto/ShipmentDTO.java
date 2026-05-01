package com.gotrack.core_logistic.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.gotrack.core_logistic.enums.ShipmentStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShipmentDTO {

    private Long id;

    @NotNull
    private Long customerId;

    @NotNull
    private Long courierId;
    
    @NotNull
    private Long flyerNumber;

    private String note;
    @NotNull
    private ShipmentStatus status;

    @NotNull
    private BigDecimal totalPrice;

    @NotNull
    private BigDecimal shipmentFee;
    
    private LocalDateTime lastUpDate;
    private LocalDateTime deliveryDate;

    @NotNull
    private PickupRequestDTO pickupRequest;



}