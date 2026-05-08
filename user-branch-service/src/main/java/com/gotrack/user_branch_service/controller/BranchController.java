package com.gotrack.user_branch_service.controller;

import com.gotrack.user_branch_service.domain.dto.BranchDTO;
import com.gotrack.user_branch_service.domain.entity.BranchEntity;
import com.gotrack.user_branch_service.domain.response.PageResponse;
import com.gotrack.user_branch_service.mappers.BranchMapper;
import com.gotrack.user_branch_service.service.BranchService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Validated
@RestController
public class BranchController {
        private BranchService branchService;

        private BranchMapper<BranchEntity, BranchDTO> branchMapper;

        public BranchController(BranchService branchService, BranchMapper<BranchEntity, BranchDTO> branchMapper) {

                this.branchService = branchService;
                this.branchMapper = branchMapper;
        }

        @PostMapping(path = "/api/branches")
        @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
        public ResponseEntity<BranchDTO> createBranch(@Valid @RequestBody BranchDTO branch,
                        HttpServletRequest request) {
                BranchEntity branchEntity = branchMapper.mapFrom(branch);
                BranchEntity savedBranchEntity = branchService.createBranch(branchEntity);
                return ResponseEntity.ok(branchMapper.mapTo(savedBranchEntity));
        }

        @GetMapping(path = "/api/branches/{id}")
        @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE', 'MERCHANT')")
        public ResponseEntity<BranchDTO> getBranchById(
                        @PathVariable @Positive(message = "Id must be positive") Long id) {
                BranchEntity branchEntity = branchService.findById(id);
                return ResponseEntity.ok(branchMapper.mapTo(branchEntity));
        }

        @GetMapping(path = "/api/branches")
        @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE', 'MERCHANT')")
        public ResponseEntity<?> listBranches(
                        @PageableDefault(size = 5, sort = "id") Pageable pageable) {

                Page<BranchEntity> page = branchService.findAll(pageable);

                List<BranchDTO> dto = page.getContent()
                                .stream()
                                .map(branchMapper::mapTo)
                                .toList();
                return ResponseEntity.ok(
                                new PageResponse<>(
                                                dto,
                                                page.getNumber(),
                                                page.getSize(),
                                                page.getTotalElements(),
                                                page.getTotalPages()));
        }

        @GetMapping(path = "/api/branches/search")
        @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE', 'MERCHANT')")
        public ResponseEntity<?> searchBranches(
                        @RequestParam(required = false) String name,
                        @RequestParam(required = false) String location,
                        @RequestParam(required = false) Boolean isDeleted,
                        @RequestParam(required = false) String phone,
                        @PageableDefault(size = 5, sort = "id") Pageable pageable) {
                Page<BranchEntity> page = branchService.search(name, location, isDeleted, phone, pageable);

                List<BranchDTO> dto = page.getContent()
                                .stream()
                                .map(branchMapper::mapTo)
                                .toList();
                return ResponseEntity.ok(
                                new PageResponse<>(
                                                dto,
                                                page.getNumber(),
                                                page.getSize(),
                                                page.getTotalElements(),
                                                page.getTotalPages()));
        }

        @PutMapping(path = "/api/branches/{id}")
        @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
        public ResponseEntity<BranchDTO> fullUpdateBranch(
                        @PathVariable @Positive(message = "Id must be positive") Long id,
                        @Valid @RequestBody BranchDTO branchDto,
                        HttpServletRequest request) {
                branchDto.setId(id);
                BranchEntity branchEntity = branchMapper.mapFrom(branchDto);
                BranchEntity updatedBranchEntity = branchService.updateBranch(branchEntity);
                return ResponseEntity.ok(branchMapper.mapTo(updatedBranchEntity));
        }

        @DeleteMapping(path = "/api/branches/{id}")
        @PreAuthorize("hasAnyRole('ADMIN')")
        public ResponseEntity<?> softDeleteBranch(
                        @PathVariable @Positive(message = "Id must be Positive") Long id,
                        HttpServletRequest request) {
                branchService.delete(id);
                return ResponseEntity.ok(
                                Map.of("message", "Branch deactivated successfully"));

        }

}
