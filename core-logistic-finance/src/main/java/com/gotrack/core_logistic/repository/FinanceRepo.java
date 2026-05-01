package com.gotrack.core_logistic.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gotrack.core_logistic.model.entity.FinancialSummary;



public interface FinanceRepo extends JpaRepository<FinancialSummary, Long> {

    Optional<FinancialSummary> findTopByOrderByIdDesc();

    
}
