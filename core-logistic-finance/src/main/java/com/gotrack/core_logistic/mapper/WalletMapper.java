package com.gotrack.core_logistic.mapper;

import org.mapstruct.Mapper;

import com.gotrack.core_logistic.model.dto.WalletDTO;
import com.gotrack.core_logistic.model.entity.Wallet;


@Mapper(componentModel = "spring")
public interface  WalletMapper {
    
      Wallet toEntity(WalletDTO DTO);
      
      WalletDTO toDto(Wallet entity);

    
}

