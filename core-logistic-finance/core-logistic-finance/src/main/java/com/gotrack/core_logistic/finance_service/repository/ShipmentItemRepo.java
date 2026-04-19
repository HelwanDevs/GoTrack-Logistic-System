package com.gotrack.core_logistic.finance_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.gotrack.core_logistic.finance_service.model.entity.ShipmentItem;


public interface ShipmentItemRepo extends JpaRepository<ShipmentItem, Long> {
    
}