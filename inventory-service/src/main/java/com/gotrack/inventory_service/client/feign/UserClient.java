package com.gotrack.inventory_service.client.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.gotrack.inventory_service.Dto.ProfileResponseDTO;

import java.util.Map;

@FeignClient(name = "user-branch-service")
public interface UserClient {

    @GetMapping("/api/users/profiles/{id}")
    Map<String, ProfileResponseDTO> getProfileById(@PathVariable("id") String id);
}
