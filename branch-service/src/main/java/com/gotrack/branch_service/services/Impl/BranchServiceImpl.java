package com.gotrack.branch_service.services.Impl;

import com.gotrack.branch_service.domain.entity.BranchEntity;
import com.gotrack.branch_service.exceptions.BranchBadRequestException;
import com.gotrack.branch_service.exceptions.BranchConflictException;
import com.gotrack.branch_service.exceptions.BranchNotFoundException;
import com.gotrack.branch_service.repository.BranchRepository;
import com.gotrack.branch_service.services.BranchService;
import org.springframework.stereotype.Service;
import org.springframework.data.jpa.domain.Specification;


import java.util.List;

@Service
public class BranchServiceImpl implements BranchService {

    private BranchRepository branchRepository;

    public BranchServiceImpl(BranchRepository branchRepository) {
        this.branchRepository = branchRepository;
    }

    @Override
    public BranchEntity createBranch(BranchEntity branchEntity) {
        if (branchRepository.existsByPhoneAndIsDeletedFalse(branchEntity.getPhone())) {
            throw new BranchConflictException("Phone already in use by another branch");
        }
        return branchRepository.save(branchEntity);
    }

    @Override
    public List<BranchEntity> findAll() {
        return branchRepository.findAll();
    }

    @Override
    public List<BranchEntity> search(String name, String location, Boolean isDeleted) {

        boolean isDeletedReturnDefault = (isDeleted!=null) ? isDeleted: false;

        Specification<BranchEntity> spec = (root, query , cb) -> {

            var predicates = cb.conjunction();
            if (name != null) {
                predicates = cb.and(predicates,
                        cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }
            if (location != null) {
                predicates = cb.and(predicates,
                        cb.like(cb.lower(root.get("location")), "%" + location.toLowerCase() + "%"));
            }

            predicates = cb.and(predicates,
                    cb.equal(root.get("isDeleted"), isDeletedReturnDefault));

            return predicates;
        };

        return branchRepository.findAll(spec);
    }

    @Override
    public BranchEntity updateBranch(BranchEntity branchEntity) {

        BranchEntity existingBranch = branchRepository.findById(branchEntity.getId())
                .orElseThrow(() -> new BranchNotFoundException("Branch not found"));
        if (branchRepository.existsByPhoneAndIdNotAndIsDeletedFalse(
                branchEntity.getPhone(), branchEntity.getId())) {

            throw new BranchConflictException("Phone already used by another branch");
        }

        applyUpdates(existingBranch, branchEntity);

        return branchRepository.save(existingBranch);
    }
    private void applyUpdates(BranchEntity existingBranch, BranchEntity newData) {
        existingBranch.setName(newData.getName());
        existingBranch.setLocation(newData.getLocation());
        existingBranch.setPhone(newData.getPhone());
    }

    @Override
    public void delete(Long id) {
        BranchEntity branch = branchRepository.findById(id)
                .orElseThrow(() -> new BranchNotFoundException("Branch not found"));
        if (Boolean.TRUE.equals(branch.getIsDeleted())) {
            throw new BranchBadRequestException("Branch already deleted");
        }
        branch.setIsDeleted(true);
        branchRepository.save(branch);
    }


}
