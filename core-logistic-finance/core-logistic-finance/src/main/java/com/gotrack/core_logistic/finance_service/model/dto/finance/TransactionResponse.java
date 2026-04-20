package com.gotrack.core_logistic.finance_service.model.dto.finance;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponse {

    private Long id;

    private Long walletId;
    private Long shipmentId;

    private BigDecimal amount;

    private String type;
    private String reason;

    private LocalDateTime createdAt;
}
