package com.gotrack.core_logistic.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.gotrack.core_logistic.enums.TransactionType;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionDTO {


    @NotNull
    private Long id;

    
    @NotNull
    @Positive(message = "Amount must be greater than zero")
    private BigDecimal amount;

    @NotNull
    private TransactionType type;

    @NotNull
    private LocalDate CreatedAt;
    
    @NotNull
    private WalletDTO wallet;

    @NotNull
    private long TransacteTo;

    @NotNull
    private long TransacteFrom;


}