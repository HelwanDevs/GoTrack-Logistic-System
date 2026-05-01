package com.gotrack.core_logistic.mapper ;

import org.mapstruct.Mapper;

import com.gotrack.core_logistic.model.dto.PickupRequestDTO;
import com.gotrack.core_logistic.model.entity.Pickup;



@Mapper(componentModel = "spring")
public interface PickupRequestMapper {
     
     PickupRequestDTO toDTO(Pickup pickup);

     Pickup toEntity(PickupRequestDTO dto);

}



