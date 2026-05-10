package com.gotrack.support_and_notifications_service.client.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(name = "auth-service")
public interface AuthClient {

    @GetMapping("/api/auth/accounts/{id}")
    Map<String, Object> getAccountById(@PathVariable("id") String id);

    @GetMapping("/api/auth/accounts/super-admin/{id}")
    Boolean isSuperAdmin(@PathVariable("id") String id);
}
