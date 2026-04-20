package com.gotrack.core_logistic.finance_service.mapper;

import com.gotrack.core_logistic.finance_service.enums.TransactionReason;
import com.gotrack.core_logistic.finance_service.enums.TransactionType;
import com.gotrack.core_logistic.finance_service.model.dto.finance.TransactionRequest;
import com.gotrack.core_logistic.finance_service.model.dto.finance.TransactionResponse;
import com.gotrack.core_logistic.finance_service.model.entity.Shipment;
import com.gotrack.core_logistic.finance_service.model.entity.Transaction;
import com.gotrack.core_logistic.finance_service.model.entity.Wallet;

public class TransactionMapper {
    

    public static Transaction toEntity(TransactionRequest request, Wallet wallet, Shipment shipment) {
        return Transaction.builder()
                .wallet(wallet)
                .shipment(shipment)
                .amount(request.getAmount())
                .type(TransactionType.valueOf(request.getType()))
                .reason(TransactionReason.valueOf(request.getReason()))
                .build();
    }

    public static TransactionResponse toResponse(Transaction entity) {
        return new TransactionResponse(
                entity.getId(),
                entity.getWallet().getId(),
                entity.getShipment().getId(), 
                entity.getAmount(),
                entity.getType().name(),
                entity.getReason().name(),
                entity.getCreatedAt()
        );
    }
}

