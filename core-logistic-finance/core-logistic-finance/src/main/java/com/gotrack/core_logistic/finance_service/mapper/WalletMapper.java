package com.gotrack.core_logistic.finance_service.mapper;

import com.gotrack.core_logistic.finance_service.model.dto.finance.WalletResponse;
import com.gotrack.core_logistic.finance_service.model.entity.Wallet;

public class WalletMapper {

    public static WalletResponse toResponse(Wallet entity) {
        return new WalletResponse(
                entity.getId(),
                entity.getProfileId(),
                entity.getBalance()
        );
    }
}

