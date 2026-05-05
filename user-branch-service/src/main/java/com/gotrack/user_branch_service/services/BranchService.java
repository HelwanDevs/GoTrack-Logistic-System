package com.gotrack.user_branch_service.services;

import com.gotrack.user_branch_service.domain.entity.BranchEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

public interface BranchService {
    BranchEntity createBranch(BranchEntity branchEntity);

    Page<BranchEntity> findAll(Pageable pageable);

    Page<BranchEntity> search(String name, String location, Boolean isDeleted, String phone ,Pageable pageable);

    BranchEntity updateBranch(BranchEntity branchEntity);

    void delete(Long id);
}

