package com.gotrack.core_logistic.model.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;

import com.gotrack.core_logistic.enums.TransactionType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Transaction {

    @ManyToOne
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

    @ManyToOne
    @JoinColumn(name = "shipment_id")
    private Shipment shipment;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    private Long id;
    
    @NotNull
    @Positive(message = "Amount must be greater than zero")
    private BigDecimal amount;

    @Column(name = "transaction_type" , nullable = false)
    @Enumerated
    private TransactionType type;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDate CreatedAt;

    @Column(name = "Transacte_to", nullable = false)
    private long TransacteTo;

    @Column(name = "Transacte_from", nullable = false)
    private long TransacteFrom;


}