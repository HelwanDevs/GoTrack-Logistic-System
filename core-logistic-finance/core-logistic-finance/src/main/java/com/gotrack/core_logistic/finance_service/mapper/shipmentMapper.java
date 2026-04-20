package com.gotrack.core_logistic.finance_service.mapper;

import com.gotrack.core_logistic.finance_service.model.dto.shipment.ShipmentRequest;
import com.gotrack.core_logistic.finance_service.model.dto.shipment.ShipmentResponse;
import com.gotrack.core_logistic.finance_service.model.entity.PickupRequest;
import com.gotrack.core_logistic.finance_service.model.entity.Shipment;

public class shipmentMapper {

    public static Shipment toEntity(ShipmentRequest request, PickupRequest pickupRequest) {
        return Shipment.builder()
                .customerId(request.getCustomerId())
                .courierId(request.getCourierId())
                .pickupRequest(pickupRequest)
                .flyerNumber(request.getFlyerNumber())
                .note(request.getNote())
                .ShipmentFee(request.getShipmentFee())
                .build();
    }

    public static ShipmentResponse toResponse(Shipment entity) {
        return new ShipmentResponse(
                entity.getId(),
                entity.getCustomerId(),
                entity.getCourierId(),
                entity.getPickupRequest().getId(),
                entity.getFlyerNumber(),
                entity.getNote(),
                entity.getStatus().name(),
                entity.getTotalPrice(),
                entity.getShipmentFee(),
                entity.getDeliveryDate()
        );
    }
}    


