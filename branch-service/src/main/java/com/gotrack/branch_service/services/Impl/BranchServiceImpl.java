package com.gotrack.branch_service.services.Impl;

import com.gotrack.branch_service.domain.entity.BranchEntity;
import com.gotrack.branch_service.repository.BranchRepository;
import com.gotrack.branch_service.services.BranchService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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

    @Override
    public List<BranchEntity> findAll() {
        return StreamSupport.stream(branchRepository
                        .findAll()
                        .spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public List<BranchEntity> search(String name, String location, Boolean isDeleted) {
        return StreamSupport.stream(branchRepository
                        .findAll()
                        .spliterator(), false)
                .filter(branch -> (name == null || branch.getName().toLowerCase().contains(name.toLowerCase())))
                .filter(branch -> (location == null || branch.getLocation().toLowerCase().contains(location.toLowerCase())))
                .filter(branch -> (isDeleted == null || branch.getIsDeleted().equals(isDeleted)))
                .collect(Collectors.toList());

    }

    @Override
    public boolean isExists(Long id) {
        return branchRepository.existsById(id);
    }

    @Override
    public BranchEntity updateBranch(BranchEntity branchEntity) {
        return branchRepository.save(branchEntity);
    }


}
