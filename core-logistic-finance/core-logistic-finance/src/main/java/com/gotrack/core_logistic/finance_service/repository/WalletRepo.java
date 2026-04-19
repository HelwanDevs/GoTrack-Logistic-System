package com.gotrack.core_logistic.finance_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.gotrack.core_logistic.finance_service.model.entity.Wallet;


public interface WalletRepo extends JpaRepository<Wallet, Long> {
    
}
