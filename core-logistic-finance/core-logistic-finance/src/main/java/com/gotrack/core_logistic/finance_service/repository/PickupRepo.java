package com.gotrack.core_logistic.finance_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gotrack.core_logistic.finance_service.model.entity.PickupRequest;



public interface PickupRepo extends JpaRepository<PickupRequest, Long> {

}
