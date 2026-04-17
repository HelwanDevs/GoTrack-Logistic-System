package com.gotrack.branch_service.repository;

import com.gotrack.branch_service.domain.entity.BranchEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository // it is like add component , so it is now a bean and can be injected anywhere needed
public interface BranchRepository extends CrudRepository<BranchEntity, Long> {
}
