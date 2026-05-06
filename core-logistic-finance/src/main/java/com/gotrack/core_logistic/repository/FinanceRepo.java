package com.gotrack.core_logistic.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gotrack.core_logistic.model.entity.FinancialSummary;



public interface FinanceRepo extends JpaRepository<FinancialSummary, Long> {

    Optional<FinancialSummary> findByCreated_at(LocalDate created_at);

    public List<FinancialSummary> findByCreatedAtBetween(LocalDate start, LocalDate end);
    
}
