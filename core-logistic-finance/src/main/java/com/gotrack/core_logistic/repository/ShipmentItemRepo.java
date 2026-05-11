package com.gotrack.core_logistic.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gotrack.core_logistic.model.entity.ShipmentItem;


public interface ShipmentItemRepo extends JpaRepository<ShipmentItem, Long> {
    
}