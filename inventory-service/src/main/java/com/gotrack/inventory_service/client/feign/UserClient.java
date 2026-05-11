package com.gotrack.inventory_service.client.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.gotrack.inventory_service.Dto.ProfileResponseDTO;

@FeignClient(name = "user-branch-service", contextId = "userClient")
public interface UserClient {

    @GetMapping("/api/users/profiles/account/{accountId}")
    ProfileResponseDTO getProfileByAccountId(@PathVariable("accountId") String accountId);

    @GetMapping("/api/users/profiles/{id}")
    ProfileResponseDTO getProfileById(@PathVariable("id") Long id);
}
