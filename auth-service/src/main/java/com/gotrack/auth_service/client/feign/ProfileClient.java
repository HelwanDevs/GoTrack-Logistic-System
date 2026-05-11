package com.gotrack.auth_service.client.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.gotrack.auth_service.dto.ProfileResponseDTO;

@FeignClient(name = "user-branch-service")
public interface ProfileClient {

    @GetMapping("/api/profiles/{id}")
    ProfileResponseDTO getProfileById(@PathVariable Long id);
}
