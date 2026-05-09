package com.gotrack.inventory_service.client.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(name = "user-branch-service")
public interface BranchClient {

    @GetMapping("/api/branches/{branchId}")
    Map<String, Object> getBranchById(@PathVariable Integer branchId);
}
