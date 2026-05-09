package com.gotrack.core_logistic.Specifications;


import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.gotrack.core_logistic.model.dto.DTOFilters.ShipmentFilter;
import com.gotrack.core_logistic.model.entity.Shipment;

import jakarta.persistence.criteria.Predicate;

public class ShipmentSpecification {

    public static Specification<Shipment> filterShipments(ShipmentFilter filter) {
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

            if (filter.getFlyerNumber() != null) {
                predicates.add(cb.equal(root.get("flyerNumber"), filter.getFlyerNumber()));
            }

            if (filter.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), filter.getStatus()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}