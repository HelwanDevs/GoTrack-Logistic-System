package com.gotrack.core_logistic.finance_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.gotrack.core_logistic.finance_service.model.entity.Shipment;


public interface ShipmentRepo extends JpaRepository<Shipment, Long> {
    
}