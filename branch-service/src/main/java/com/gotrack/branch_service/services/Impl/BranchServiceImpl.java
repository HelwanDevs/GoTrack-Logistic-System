package com.gotrack.branch_service.services.Impl;

import com.gotrack.branch_service.domain.entity.BranchEntity;
import com.gotrack.branch_service.repository.BranchRepository;
import com.gotrack.branch_service.services.BranchService;
import org.springframework.stereotype.Service;

@Service
public class BranchServiceImpl implements BranchService {

    private BranchRepository branchRepository;

    public BranchServiceImpl(BranchRepository branchRepository) {
        this.branchRepository = branchRepository;
    }

    @Override
    public BranchEntity createBranch(BranchEntity branchEntity) {
       return branchRepository.save(branchEntity);
    }
}
