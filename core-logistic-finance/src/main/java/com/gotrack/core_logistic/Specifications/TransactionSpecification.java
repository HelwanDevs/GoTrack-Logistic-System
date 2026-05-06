package com.gotrack.core_logistic.Specifications;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import com.gotrack.core_logistic.model.dto.Filters.TransactionFilter;
import com.gotrack.core_logistic.model.entity.Transaction;

public class TransactionSpecification {
    

    public static Specification<Transaction> filterTransactions(TransactionFilter filter) {
        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (filter.getFromProfileId() != null) {
                predicates.add(cb.equal(root.get("fromProfileId"), filter.getFromProfileId()));
            }

            if (filter.getToProfileId() != null) {
                predicates.add(cb.equal(root.get("toProfileId"), filter.getToProfileId()));
            }

            if (filter.getType() != null && !filter.getType().isEmpty()) {
                predicates.add(cb.equal(root.get("type"), filter.getType()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}
