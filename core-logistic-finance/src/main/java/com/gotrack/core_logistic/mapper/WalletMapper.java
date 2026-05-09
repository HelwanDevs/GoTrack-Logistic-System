package com.gotrack.core_logistic.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.gotrack.core_logistic.model.dto.WalletDTO;
import com.gotrack.core_logistic.model.entity.Wallet;



@Mapper(componentModel = "spring")
public interface  WalletMapper {
    
      @Mapping(target = "transactions", source = "transactions" , ignore = true)
      Wallet toEntity(WalletDTO DTO);
      
      WalletDTO toDto(Wallet entity);

    
}

