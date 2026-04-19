package com.gotrack.core_logistic.finance_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.gotrack.core_logistic.finance_service.model.entity.Transaction;

public interface TransactionRepo extends JpaRepository<Transaction, Long> {
    
}