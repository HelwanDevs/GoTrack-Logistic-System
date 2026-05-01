package com.gotrack.core_logistic.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.gotrack.core_logistic.enums.TransactionType;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionDTO {

    private Long id;

    
    @NotNull
    private BigDecimal amount;

    @NotNull
    private TransactionType type;

    @NotNull
    private LocalDateTime CreatedAt;

    private ShipmentDTO shipment;
    
    @NotNull
    private WalletDTO wallet;

    @NotNull
    private long TransacteTo;

    @NotNull
    private long TransacteFrom;


}