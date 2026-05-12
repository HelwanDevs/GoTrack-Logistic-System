package com.gotrack.core_logistic.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.gotrack.core_logistic.enums.ShipmentStatus;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShipmentDTO {

    private Long id;

    @NotNull
    @Positive(message = "MERCHANT ID must be positive")
    private Long MERCHANTId;

    @NotNull
    @Positive(message = "Courier ID must be positive")
    private Long courierId;

    @NotNull
    private Long flyerNumber;

    private String note;

    private ShipmentStatus status;

    
    @Positive(message = "Total price must be greater than zero")
    private BigDecimal totalPrice;

    @NotNull
    @Positive(message = "Shipment fee must be greater than zero")
    private BigDecimal shipmentFee;

    private LocalDateTime lastUpDate;

    @Future(message = "Invalid date formatting")
    private LocalDateTime deliveryDate;

    @NotNull
    private PickupRequestDTO pickupRequest;

}