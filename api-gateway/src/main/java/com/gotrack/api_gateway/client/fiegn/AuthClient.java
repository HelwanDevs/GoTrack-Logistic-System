package com.gotrack.api_gateway.client.fiegn;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;


@FeignClient(name = "auth-service")
public interface AuthClient {
    
    @PostMapping("/api/auth/validate")
    public Boolean validateUserRefresToken(String accountId);
}
