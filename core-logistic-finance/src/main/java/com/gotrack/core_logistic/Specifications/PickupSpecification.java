package com.gotrack.core_logistic.Specifications;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.gotrack.core_logistic.model.dto.DTOFilters.PickupFilter;
import com.gotrack.core_logistic.model.entity.Pickup;

import jakarta.persistence.criteria.Predicate;

public class PickupSpecification {

    public static Specification<Pickup> filterPickups(PickupFilter filter) {
        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (filter.getId() != null) {
                predicates.add(cb.equal(root.get("id"), filter.getId()));
            }

            if (filter.getCustomerId() != null) {
                predicates.add(cb.equal(root.get("customerId"), filter.getCustomerId()));
            }

            if (filter.getCourierId() != null) {
                predicates.add(cb.equal(root.get("courierId"), filter.getCourierId()));
            }

            if (filter.getStatus() != null) {
                predicates.add(cb.equal(
                        cb.lower(root.get("status").as(String.class)),
                        filter.getStatus().toLowerCase()
                ));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}