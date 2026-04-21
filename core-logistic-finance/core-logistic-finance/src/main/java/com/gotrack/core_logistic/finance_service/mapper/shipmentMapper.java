package com.gotrack.core_logistic.finance_service.mapper;

import com.gotrack.core_logistic.finance_service.model.dto.ShipmentDTO;
import com.gotrack.core_logistic.finance_service.model.entity.Pickup;
import com.gotrack.core_logistic.finance_service.model.entity.Shipment;

public class shipmentMapper {

    public class ShipmentMapper {

    public static Shipment toEntity(ShipmentDTO request, Pickup pickupRequest) {

        Shipment entity = new Shipment();

        entity.setCustomerId(request.getCustomerId());
        entity.setCourierId(request.getCourierId());
        entity.setPickupRequest(pickupRequest);
        entity.setFlyerNumber(request.getFlyerNumber());
        entity.setNote(request.getNote());
        entity.setShipmentFee(request.getShipmentFee());
        entity.setTotalPrice(request.getTotalPrice());
        entity.setDeliveryDate(request.getDeliveryDate());
        entity.setStatus(request.getStatus());

        return entity;
    }

    public static ShipmentDTO toDto(Shipment entity) {

        ShipmentDTO dto = new ShipmentDTO();

        dto.setId(entity.getId());
        dto.setCustomerId(entity.getCustomerId());
        dto.setCourierId(entity.getCourierId());
        dto.setPickupRequestId(entity.getPickupRequest().getId());
        dto.setFlyerNumber(entity.getFlyerNumber());
        dto.setNote(entity.getNote());
        dto.setStatus(entity.getStatus());
        dto.setTotalPrice(entity.getTotalPrice());
        dto.setShipmentFee(entity.getShipmentFee());
        dto.setDeliveryDate(entity.getDeliveryDate());

        return dto;
    }
}
}    


