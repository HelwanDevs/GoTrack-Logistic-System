package com.gotrack.branch_service.controller;


import com.gotrack.branch_service.domain.dto.BranchDTO;
import com.gotrack.branch_service.domain.entity.BranchEntity;
import com.gotrack.branch_service.mappers.Mapper;
import com.gotrack.branch_service.services.BranchService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class BranchController {
    private BranchService branchService;

    private Mapper<BranchEntity, BranchDTO> branchMapper;

    public BranchController(BranchService branchService , Mapper<BranchEntity, BranchDTO> branchMapper){

        this.branchService = branchService;
        this.branchMapper= branchMapper;
    }

    @PostMapping(path = "/api/branches")
    public BranchDTO createBranch(@Valid @RequestBody BranchDTO branch){
        BranchEntity branchEntity = branchMapper.mapFrom(branch);
        BranchEntity savedBranchEntity = branchService.createBranch(branchEntity);
        return branchMapper.mapTo(savedBranchEntity);

    }

    @GetMapping(path= "/api/branches")
    public ResponseEntity<?> listBranches(@PageableDefault(size = 5, sort = "id")
                                              Pageable pageable) {

        Page<BranchEntity> page = branchService.findAll(pageable);

        List<BranchDTO> dto = page.getContent()
                .stream()
                .map(branchMapper::mapTo)
                .toList();
        return ResponseEntity.ok(
                Map.of(
                        "content", dto,
                        "page", page.getNumber(),
                        "size", page.getSize(),
                        "totalElements", page.getTotalElements(),
                        "totalPages", page.getTotalPages()
                )
        );
    }

    @GetMapping(path= "/api/branches/search")
    public ResponseEntity<?> searchBranches(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Boolean isDeleted,
            @PageableDefault(size = 5, sort = "id")
            Pageable pageable
    ){
        Page<BranchEntity> page = branchService.search(name, location, isDeleted, pageable);

        List<BranchDTO> dto = page.getContent()
                .stream()
                .map(branchMapper::mapTo)
                .toList();
        return ResponseEntity.ok(
                Map.of(
                        "content", dto,
                        "page", page.getNumber(),
                        "size", page.getSize(),
                        "totalElements", page.getTotalElements(),
                        "totalPages", page.getTotalPages()
                )
        );
    }

    @PutMapping(path = "/api/branches/{id}")
    public ResponseEntity<BranchDTO> fullUpdateBranch(@PathVariable Long id ,
                                                      @Valid @RequestBody BranchDTO branchDto){
        branchDto.setId(id);
        BranchEntity branchEntity= branchMapper.mapFrom(branchDto);
        BranchEntity updatedBranchEntity = branchService.updateBranch(branchEntity);
        return ResponseEntity.ok(branchMapper.mapTo(updatedBranchEntity));
    }
    @DeleteMapping(path = "/api/branches/{id}")
    public ResponseEntity<?> softDeleteBranch(@PathVariable Long id){

        branchService.delete(id);
        return ResponseEntity.ok(
                Map.of("message", "Branch deactivated successfully"));

    }


}
