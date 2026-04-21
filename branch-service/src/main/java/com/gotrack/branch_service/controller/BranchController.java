package com.gotrack.branch_service.controller;


import com.gotrack.branch_service.domain.dto.BranchDTO;
import com.gotrack.branch_service.domain.entity.BranchEntity;
import com.gotrack.branch_service.mappers.Mapper;
import com.gotrack.branch_service.services.BranchService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class BranchController {
    private BranchService branchService;

    private Mapper<BranchEntity, BranchDTO> branchMapper;

    public BranchController(BranchService branchService , Mapper<BranchEntity, BranchDTO> branchMapper){

        this.branchService = branchService;
        this.branchMapper= branchMapper;
    }

    @PostMapping(path = "/api/branches")
    public BranchDTO createBranch(@RequestBody BranchDTO branch){
        BranchEntity branchEntity = branchMapper.mapFrom(branch);
        BranchEntity savedBranchEntity = branchService.createBranch(branchEntity);
        return branchMapper.mapTo(savedBranchEntity);

    }

    @GetMapping(path= "/api/branches")
    public List<BranchDTO> listBranches(){
        List<BranchEntity> branches = branchService.findAll();
        return branches.stream()
                .map(branchMapper::mapTo)
                .collect((Collectors.toList()));

    }

    @GetMapping(path= "/api/branches/search")
    public List<BranchDTO> searchBranches(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Boolean isDeleted
    ){
        List<BranchEntity> branches= branchService.search(name, location , isDeleted);
        return branches.stream()
                .map(branchMapper::mapTo)
                .collect((Collectors.toList()));
    }

    @PutMapping(path = "/api/branches/{id}")
    public ResponseEntity<BranchDTO> fullUpdateBranch(@PathVariable("id") Long id ,
                                                      @RequestBody BranchDTO branchDto){
        if(!branchService.isExists(id)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        branchDto.setId(id);
        BranchEntity branchEntity= branchMapper.mapFrom(branchDto);
        BranchEntity updatedBranchEntity = branchService.updateBranch(branchEntity);
        return new ResponseEntity<>(
                branchMapper.mapTo(updatedBranchEntity),
                HttpStatus.OK);
    }
    @DeleteMapping(path = "/api/branches/{id}")
    public ResponseEntity softDeleteBranch(@PathVariable("id") Long id){
        if(!branchService.isExists(id)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        branchService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }


}
