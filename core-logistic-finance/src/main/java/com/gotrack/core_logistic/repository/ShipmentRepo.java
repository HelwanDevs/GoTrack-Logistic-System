package com.gotrack.core_logistic.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import com.gotrack.core_logistic.model.entity.Shipment;


public interface ShipmentRepo extends JpaRepository<Shipment, Long> {
    
          Page<Shipment> findAll(Specification<Shipment> spec, Pageable pageable);

}