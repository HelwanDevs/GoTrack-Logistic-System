package com.gotrack.core_logistic.finance_service.model.dto.finance;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WalletResponse {

    private Long id;

    private Long profileId;
    private BigDecimal balance;
}