package com.gotrack.core_logistic.finance_service.mapper ;

import com.gotrack.core_logistic.finance_service.model.dto.pickup.PickupRequestRequest;
import com.gotrack.core_logistic.finance_service.model.dto.pickup.PickupRequestResponse;
import com.gotrack.core_logistic.finance_service.model.entity.PickupRequest;

public class PickupRequestMapper {

    public static PickupRequest toEntity(PickupRequestRequest request) {
        return PickupRequest.builder()
                .customerId(request.getCustomerId())
                .courierId(request.getCourierId())
                .pickupAddress(request.getPickupAddress())
                .notes(request.getNotes())
                .receiverName(request.getReceiverName())
                .receiverContact(request.getReceiverContact())
                .receiverAddress(request.getReceiverAddress())
                .build();
    }

    public static PickupRequestResponse toResponse(PickupRequest entity) {
        return new PickupRequestResponse(
                entity.getId(),
                entity.getCustomerId(),
                entity.getCourierId(),
                entity.getPickupAddress(),
                entity.getNotes(),
                entity.getReceiverName(),
                entity.getReceiverContact(),
                entity.getReceiverAddress(),
                entity.getStatus().name(),
                entity.getLastUpdate()
        );
    }
}

