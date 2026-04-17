package com.gotrack.branch_service.services;

import com.gotrack.branch_service.domain.entity.BranchEntity;
import org.springframework.stereotype.Service;

@Service
public interface BranchService {
    BranchEntity createBranch(BranchEntity branchEntity);
}
