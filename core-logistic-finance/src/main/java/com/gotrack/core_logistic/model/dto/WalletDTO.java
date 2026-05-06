package com.gotrack.core_logistic.model.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;


@Getter
@Setter
public class WalletDTO {

    
    private Long id;
    
    @NonNull
    private Long profileId;

    @NonNull
    private BigDecimal balance = BigDecimal.ZERO;

    private List<TransactionDTO> transactions;


}