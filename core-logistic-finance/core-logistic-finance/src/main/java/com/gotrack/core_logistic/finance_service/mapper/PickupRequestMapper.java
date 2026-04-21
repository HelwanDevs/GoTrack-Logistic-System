package com.gotrack.core_logistic.finance_service.mapper ;

import com.gotrack.core_logistic.finance_service.model.dto.PickupRequestDTO;
import com.gotrack.core_logistic.finance_service.model.entity.Pickup;

public class PickupRequestMapper {

    public static  PickupRequestDTO toDTO(Pickup request) {

        PickupRequestDTO dto = new PickupRequestDTO();
        dto.setId(request.getId());
        dto.setCustomerId(request.getCustomerId());
        dto.setCourierId(request.getCourierId());
        dto.setPickupAddress(request.getPickupAddress());
        dto.setNotes(request.getNotes());
        dto.setReceiverName(request.getReceiverName());
        dto.setReceiverContact(request.getReceiverContact());
        dto.setReceiverAddress(request.getReceiverAddress());
        dto.setStatus(request.getStatus());
        dto.setCost(request.getCost());
        dto.setLastUpdate(request.getLastUpdate());
        return dto;
    }

   public static Pickup toEntity(PickupRequestDTO pickupRequest) {

        Pickup entity = new Pickup();

        entity.setId(pickupRequest.getId());
        entity.setCustomerId(pickupRequest.getCustomerId());
        entity.setCourierId(pickupRequest.getCourierId());
        entity.setPickupAddress(pickupRequest.getPickupAddress());
        entity.setNotes(pickupRequest.getNotes());
        entity.setReceiverName(pickupRequest.getReceiverName());
        entity.setReceiverContact(pickupRequest.getReceiverContact());
        entity.setReceiverAddress(pickupRequest.getReceiverAddress());
        entity.setStatus(pickupRequest.getStatus());
        entity.setLastUpdate(pickupRequest.getLastUpdate());
        entity.setCost(pickupRequest.getCost());
        entity.setPickupTime(pickupRequest.getPickupTime());
        


        return entity;
    }
}

