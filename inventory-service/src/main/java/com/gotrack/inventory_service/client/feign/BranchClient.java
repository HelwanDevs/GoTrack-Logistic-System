package com.gotrack.inventory_service.client.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.gotrack.inventory_service.Dto.BranchResponseDTO;

@FeignClient(name = "user-branch-service", contextId = "branchClient")
public interface BranchClient {

    @GetMapping("/api/branches/{branchId}")
    BranchResponseDTO getBranchById(@PathVariable Long branchId);
}
