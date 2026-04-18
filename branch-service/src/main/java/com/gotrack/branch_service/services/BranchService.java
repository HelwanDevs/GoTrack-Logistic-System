package com.gotrack.branch_service.services;

import com.gotrack.branch_service.domain.entity.BranchEntity;
import org.springframework.stereotype.Service;

import java.util.List;

public interface BranchService {
    BranchEntity createBranch(BranchEntity branchEntity);

    List<BranchEntity> findAll();
}

