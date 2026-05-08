package com.gotrack.user_branch_service.service.imp;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gotrack.user_branch_service.domain.entity.BranchEntity;
import com.gotrack.user_branch_service.exceptions.BadRequestException;
import com.gotrack.user_branch_service.exceptions.ConflictException;
import com.gotrack.user_branch_service.exceptions.NotFoundException;
import com.gotrack.user_branch_service.repository.BranchRepository;
import com.gotrack.user_branch_service.service.BranchService;

@Service
public class BranchServiceImp implements BranchService {

    private BranchRepository branchRepository;

    public BranchServiceImp(BranchRepository branchRepository) {
        this.branchRepository = branchRepository;
    }

    @Override
    public BranchEntity createBranch(BranchEntity branchEntity) {
        if (branchRepository.existsByPhoneAndIsDeletedFalse(branchEntity.getPhone())) {
            throw new ConflictException("Phone already in use by another branch");
        }

        return branchRepository.save(branchEntity);
    }

    @Override
    public BranchEntity findById(Long id) {
        BranchEntity branch = branchRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Branch not found"));
        return branch;
    }

    @Override
    public Page<BranchEntity> findAll(Pageable pageable) {
        return branchRepository.findAll(pageable);
    }

    @Override
    public Page<BranchEntity> search(String name, String location, Boolean isDeleted, String phone, Pageable pageable) {
        boolean isDeletedReturnDefault = (isDeleted != null) ? isDeleted : false;

        Specification<BranchEntity> spec = (root, query, cb) -> {
            var predicates = cb.conjunction();

            if (name != null && !name.isBlank()) {
                String normalizedName = name.trim().toLowerCase();
                predicates = cb.and(predicates,
                        cb.like(cb.lower(root.get("name")), "%" + normalizedName + "%"));
            }

            if (location != null && !location.isBlank()) {
                String normalizedLocation = location.trim().toLowerCase();
                predicates = cb.and(predicates,
                        cb.like(cb.lower(root.get("location")), "%" + normalizedLocation + "%"));
            }

            if (phone != null && !phone.isBlank()) {
                String normalizedPhone = phone.trim();
                predicates = cb.and(predicates,
                        cb.equal(root.get("phone"), normalizedPhone));
            }
            predicates = cb.and(predicates,
                    cb.equal(root.get("isDeleted"), isDeletedReturnDefault));
            return predicates;
        };
        return branchRepository.findAll(spec, pageable);
    }

    @Override
    public BranchEntity updateBranch(BranchEntity branchEntity) {

        BranchEntity existingBranch = branchRepository.findById(branchEntity.getId())
                .orElseThrow(() -> new NotFoundException("Branch not found"));
        if (Boolean.TRUE.equals(existingBranch.getIsDeleted())) {
            throw new BadRequestException("Cannot update a deleted branch");
        }

        if (branchRepository.existsByPhoneAndIdNotAndIsDeletedFalse(
                branchEntity.getPhone(), branchEntity.getId())) {

            throw new ConflictException("Phone already used by another branch");
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
                .orElseThrow(() -> new NotFoundException("Branch not found"));
        if (Boolean.TRUE.equals(branch.getIsDeleted())) {
            throw new BadRequestException("Branch already deleted");
        }
        branch.setIsDeleted(true);
        branchRepository.save(branch);
    }

}
