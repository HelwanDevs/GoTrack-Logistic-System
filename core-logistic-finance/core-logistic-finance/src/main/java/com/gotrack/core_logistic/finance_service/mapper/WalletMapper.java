package com.gotrack.core_logistic.finance_service.mapper;

import com.gotrack.core_logistic.finance_service.model.dto.finance.WalletResponseDTO;
import com.gotrack.core_logistic.finance_service.model.entity.Wallet;

public class WalletMapper {

    public static WalletResponseDTO toResponse(Wallet entity) {
        return new WalletResponseDTO(
                entity.getId(),
                entity.getProfileId(),
                entity.getBalance()
        );
    }
}

