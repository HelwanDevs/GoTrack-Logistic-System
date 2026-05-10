package com.gotrack.support_and_notifications_service.client.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.gotrack.support_and_notifications_service.client.dto.AccountDTO;
import com.gotrack.support_and_notifications_service.config.FeignConfig;

@FeignClient(name = "auth-service", contextId = "authAccountClient", configuration = FeignConfig.class)
public interface AuthAccountClient {
    @GetMapping("/api/auth/accounts/{id}")
    AccountDTO getAccount(@PathVariable("id") String id);
}
