package com.gotrack.auth_service.client.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import com.gotrack.auth_service.dto.ProfileResponseDTO;

@FeignClient(name = "user-branch-service")
public interface ProfileClient {

    @GetMapping("/api/users/profiles/{id}")
    ProfileResponseDTO getProfileById(@PathVariable Long id);

    @PutMapping("/api/users/profiles/linkProfileToAccount/{accountId}/{profileId}")
    ProfileResponseDTO linkProfileToAccount(@PathVariable String accountId, @PathVariable Long profileId);
}
