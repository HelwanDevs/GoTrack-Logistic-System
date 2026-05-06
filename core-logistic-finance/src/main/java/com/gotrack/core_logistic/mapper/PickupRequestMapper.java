package com.gotrack.core_logistic.mapper ;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.gotrack.core_logistic.model.dto.PickupRequestDTO;
import com.gotrack.core_logistic.model.entity.Pickup;



@Mapper(componentModel = "spring")
public interface PickupRequestMapper {
     
     PickupRequestDTO toDTO(Pickup pickup);

     @Mapping(target = "shipments", ignore = true)
     Pickup toEntity(PickupRequestDTO dto);

}



