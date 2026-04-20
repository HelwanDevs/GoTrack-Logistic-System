package com.gotrack.branch_service.services.Impl;

import com.gotrack.branch_service.domain.entity.BranchEntity;
import com.gotrack.branch_service.repository.BranchRepository;
import com.gotrack.branch_service.services.BranchService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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

        String optimizedName= (name!= null)? name.toLowerCase():null;
        String optimizedLocation= (location!= null)? location.toLowerCase():null;

        boolean isDeletedReturnDefault = (isDeleted!=null) ? isDeleted: false;

        return StreamSupport.stream(branchRepository
                .findAll()
                .spliterator(), false)
                .filter(branch-> {
                    if(optimizedName ==null) return true;
                    return branch.getName() != null &&
                            branch.getName().toLowerCase().contains(optimizedName);
                })
                .filter(branch -> {
                    if (optimizedLocation == null) return true;
                    return branch.getLocation() != null &&
                            branch.getLocation().toLowerCase().contains(optimizedLocation);
                })
                .filter(branch -> Boolean.TRUE.equals(branch.getIsDeleted()) == isDeletedReturnDefault)

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

    @Override
    public void delete(Long id) {
        BranchEntity branch = branchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Branch not found"));

        branch.setIsDeleted(true);
        branchRepository.save(branch);
    }


}
