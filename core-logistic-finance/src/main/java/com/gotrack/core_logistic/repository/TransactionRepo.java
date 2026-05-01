package com.gotrack.core_logistic.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gotrack.core_logistic.model.entity.Transaction;

public interface TransactionRepo extends JpaRepository<Transaction, Long> {
    
}