package com.gotrack.branch_service.repository;

import com.gotrack.branch_service.domain.entity.BranchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository // it is like add component , so it is now a bean and can be injected anywhere needed
public interface BranchRepository extends JpaRepository<BranchEntity, Long>,
        JpaSpecificationExecutor<BranchEntity> {

    boolean existsByPhoneAndIsDeletedFalse(String phone); // create

    boolean existsByPhoneAndIdNotAndIsDeletedFalse(String phone, Long id); // update (same phone , not this id , active)


}
