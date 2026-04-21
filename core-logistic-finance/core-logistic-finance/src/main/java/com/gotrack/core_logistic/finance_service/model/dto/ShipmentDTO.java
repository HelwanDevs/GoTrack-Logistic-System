package com.gotrack.core_logistic.finance_service.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShipmentDTO {

    private Long id;

    private Long customerId;
    private Long courierId;

    private Long pickupRequestId;

    private Long flyerNumber;
    private String note;

    private String status;

    private BigDecimal totalPrice;
    private BigDecimal shipmentFee;

    private LocalDateTime deliveryDate;
}