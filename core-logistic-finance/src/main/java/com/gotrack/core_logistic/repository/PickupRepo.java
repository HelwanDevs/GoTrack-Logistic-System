package com.gotrack.core_logistic.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import com.gotrack.core_logistic.model.entity.Pickup;



public interface PickupRepo extends JpaRepository<Pickup, Long> {

      Page<Pickup> findAll(Specification<Pickup> spec, Pageable pageable);


}
