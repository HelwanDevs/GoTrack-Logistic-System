package com.gotrack.core_logistic.finance_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gotrack.core_logistic.finance_service.model.entity.Pickup;



public interface PickupRepo extends JpaRepository<Pickup, Long> {

  

}
