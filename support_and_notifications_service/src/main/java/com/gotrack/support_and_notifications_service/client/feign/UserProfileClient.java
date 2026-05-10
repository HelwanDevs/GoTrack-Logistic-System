package com.gotrack.support_and_notifications_service.client.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.gotrack.support_and_notifications_service.client.dto.ProfileDTO;

@FeignClient(name = "user-branch-service", contextId = "userProfileClient")
public interface UserProfileClient {

    @GetMapping("/api/users/profiles/{id}")
    ProfileDTO getProfile(@PathVariable("id") Long id);
}
