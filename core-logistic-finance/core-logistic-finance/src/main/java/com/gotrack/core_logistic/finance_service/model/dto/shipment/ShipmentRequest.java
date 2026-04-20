package com.gotrack.core_logistic.finance_service.model.dto.shipment;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShipmentRequest {

    private Long customerId;
    private Long courierId;
    private Long pickupRequestId;

    private Long flyerNumber;
    private String note;

    private BigDecimal shipmentFee;
}