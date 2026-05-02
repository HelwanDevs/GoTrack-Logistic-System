package com.gotrack.core_logistic.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gotrack.core_logistic.model.entity.Wallet;


public interface WalletRepo extends JpaRepository<Wallet, Long> {
    

    Optional<Wallet> findByProfileId(Long userId);
    
}
