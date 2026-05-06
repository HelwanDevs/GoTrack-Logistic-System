package com.gotrack.core_logistic.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import com.gotrack.core_logistic.model.dto.TransactionDTO;
import com.gotrack.core_logistic.model.entity.Transaction;

public interface TransactionRepo extends JpaRepository<Transaction, Long> {

    Page<Transaction> findByFromProfileIdOrToProfileId(Long fromId, Long toId, Pageable pageable);

    Page<TransactionDTO> findAll(Specification<Transaction> spec, Pageable pageable);
    
}