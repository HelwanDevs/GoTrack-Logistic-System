package com.gotrack.core_logistic.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class FinancialSummaryDTO {
    
    
    private Long id;
    
    @NotNull
    private BigDecimal NetCash;
    @NotNull
    private BigDecimal vendorDue; //المستحق للبائعين
    
    @NotNull
    private BigDecimal venderPaid;   //دفعات البائعين

    @NotNull
    private BigDecimal shippingCost;

    @NotNull
    private BigDecimal  curierCommission ;

    @NotNull
    private BigDecimal totalCommission ;

    @NotNull
    private BigDecimal expenses ;

    @NotNull
    private BigDecimal profitMargin ;
    
    @NotNull
    private BigDecimal netProfit ;

    private LocalDate createdAt;
}
