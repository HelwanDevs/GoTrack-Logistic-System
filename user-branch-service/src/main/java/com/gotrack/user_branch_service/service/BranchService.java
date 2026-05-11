package com.gotrack.user_branch_service.service;

import com.gotrack.user_branch_service.domain.entity.BranchEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface BranchService {
    BranchEntity createBranch(BranchEntity branchEntity);

    Page<BranchEntity> findAll(Pageable pageable);

    Page<BranchEntity> search(String name, String location, Boolean isDeleted, String phone, Pageable pageable);

    BranchEntity updateBranch(BranchEntity branchEntity);

    BranchEntity findById(Long id);

    void delete(Long id);
}
