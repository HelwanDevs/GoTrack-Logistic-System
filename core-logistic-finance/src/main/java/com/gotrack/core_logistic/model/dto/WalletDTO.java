package com.gotrack.core_logistic.model.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class WalletDTO {

    private Long id;

    private Long profileId;
    private BigDecimal balance;

    
    private List<TransactionDTO> transactions;


}