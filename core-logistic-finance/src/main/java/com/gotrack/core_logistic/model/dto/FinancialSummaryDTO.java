package com.gotrack.core_logistic.model.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class FinancialSummaryDTO {
    
    private BigDecimal NetCash;

    private BigDecimal vendorDue; //المستحق للبائعين
    private BigDecimal venderPaid;   //دفعات البائعين
    private BigDecimal totalCollected; 

    private BigDecimal shippingCost;

    private BigDecimal  curierCommission ;

    private BigDecimal totalCommission ;

    private  BigDecimal  managersBalance ;
    
    private  BigDecimal curierBalance ;

    private BigDecimal expenses ;

    private BigDecimal profitMargin ;
    
    private BigDecimal netProfit ;

}
