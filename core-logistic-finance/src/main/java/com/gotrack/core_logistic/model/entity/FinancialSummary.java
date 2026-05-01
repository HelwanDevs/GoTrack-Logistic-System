package com.gotrack.core_logistic.model.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="Financial_Summary")
public class FinancialSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(columnDefinition = "DECIMAL(10,2) DEFAULT 0")
    private BigDecimal NetCash=BigDecimal.ZERO;
    
    @Column(columnDefinition = "DECIMAL(10,2) DEFAULT 0")
    private BigDecimal vendorDue= BigDecimal.ZERO; //المستحق للبائعين

    @Column(columnDefinition = "DECIMAL(10,2) DEFAULT 0")
    private BigDecimal venderPaid= BigDecimal.ZERO;   //دفعات البائعين

    @Column(columnDefinition = "DECIMAL(10,2) DEFAULT 0")
    private BigDecimal totalCollected = BigDecimal.ZERO; 

    @Column(columnDefinition = "DECIMAL(10,2) DEFAULT 0")
    private BigDecimal shippingCost = BigDecimal.ZERO;

    @Column(columnDefinition = "DECIMAL(10,2) DEFAULT 0")
    private BigDecimal curierCommission = BigDecimal.ZERO;

    @Column(columnDefinition = "DECIMAL(10,2) DEFAULT 0")
    private BigDecimal totalCommission = BigDecimal.ZERO;

    @Column(columnDefinition = "DECIMAL(10,2) DEFAULT 0")
    private  BigDecimal  managersBalance = BigDecimal.ZERO;
    
    @Column(columnDefinition = "DECIMAL(10,2) DEFAULT 0")
    private  BigDecimal curierBalance  = BigDecimal.ZERO;

    @Column(columnDefinition = "DECIMAL(10,2) DEFAULT 0")
    private BigDecimal expenses = BigDecimal.ZERO;

    @Column(columnDefinition = "DECIMAL(10,2) DEFAULT 0")
    private BigDecimal profitMargin = BigDecimal.ZERO;
    
    private BigDecimal netProfit = BigDecimal.ZERO;


}
