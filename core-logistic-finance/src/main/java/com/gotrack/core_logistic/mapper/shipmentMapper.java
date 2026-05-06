package com.gotrack.core_logistic.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.gotrack.core_logistic.model.dto.ShipmentDTO;
import com.gotrack.core_logistic.model.entity.Shipment;



@Mapper(componentModel = "spring" , uses={PickupRequestMapper.class})
public interface shipmentMapper {

    @Mapping(target = "transactions", ignore = true)
    @Mapping(target = "items", ignore = true)
    Shipment toEntity(ShipmentDTO request);

    ShipmentDTO toDto(Shipment entity);
}