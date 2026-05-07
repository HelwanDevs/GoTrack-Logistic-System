package com.gotrack.inventory_service.Specifications;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.gotrack.inventory_service.Dto.inventoryFilter;
import com.gotrack.inventory_service.Entity.InventoryItem;

import jakarta.persistence.criteria.Predicate;

public class inventorySpecifications {
      public static Specification<InventoryItem> filterInventory(inventoryFilter filter) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (filter.getBranchId() != null) {
                predicates.add(
                        cb.equal(root.get("branchId"), filter.getBranchId())
                );
            }
            if (filter.getUniqueSku() != null &&
                    !filter.getUniqueSku().trim().isEmpty()) {

                predicates.add(
                        cb.like(
                                cb.lower(root.get("uniqueSku")),
                                "%" + filter.getUniqueSku().toLowerCase() + "%"
                        )
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
