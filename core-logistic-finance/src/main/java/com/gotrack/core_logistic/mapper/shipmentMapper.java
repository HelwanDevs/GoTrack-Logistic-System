package com.gotrack.core_logistic.mapper;

import org.mapstruct.Mapper;

import com.gotrack.core_logistic.model.dto.ShipmentDTO;
import com.gotrack.core_logistic.model.entity.Shipment;



@Mapper(componentModel = "spring" , uses={PickupRequestMapper.class})
public interface shipmentMapper {

    
    Shipment toEntity(ShipmentDTO request);

    ShipmentDTO toDto(Shipment entity);
}