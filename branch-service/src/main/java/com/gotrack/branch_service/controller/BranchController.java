package com.gotrack.branch_service.controller;


import com.gotrack.branch_service.domain.dto.BranchDTO;
import com.gotrack.branch_service.domain.entity.BranchEntity;
import com.gotrack.branch_service.mappers.Mapper;
import com.gotrack.branch_service.services.BranchService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BranchController {
    private BranchService branchService;

    private Mapper<BranchEntity, BranchDTO> branchMapper;

    public BranchController(BranchService branchService , Mapper<BranchEntity, BranchDTO> branchMapper){

        this.branchService = branchService;
        this.branchMapper= branchMapper;
    }

    @PostMapping(path = "/branchs")
    public BranchDTO createBranch(@RequestBody BranchDTO branch){
        BranchEntity branchEntity = branchMapper.mapFrom(branch);
        BranchEntity savedBranchEntity = branchService.createBranch(branchEntity);
        return branchMapper.mapTo(savedBranchEntity);

    }
}
