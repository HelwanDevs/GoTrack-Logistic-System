package com.gotrack.core_logistic.finance_service.model.dto.finance;

import java.math.BigDecimal;

import lombok.Getter;

@Getter
public class TransactionRequestDTO {

    private Long walletId;
    private Long shipmentId;

    private BigDecimal amount;

    private String type;
    private String reason;
}