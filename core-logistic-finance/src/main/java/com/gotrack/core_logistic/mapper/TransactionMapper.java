package com.gotrack.core_logistic.mapper;

import org.mapstruct.Mapper;

import com.gotrack.core_logistic.model.dto.TransactionDTO;
import com.gotrack.core_logistic.model.entity.Transaction;


@Mapper(componentModel = "spring", uses={shipmentMapper.class , WalletMapper.class})
public interface  TransactionMapper {
    
    Transaction toEntity(TransactionDTO DTO);
   
    
    TransactionDTO toDTO(Transaction savedTransaction);
    

}

