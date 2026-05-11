package com.gotrack.auth_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;

@Configuration
public class FeignConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {

        return requestTemplate -> {

            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes();

            if (attributes == null) {
                return;
            }

            HttpServletRequest request = attributes.getRequest();

            String authorizationHeader = request.getHeader("X-Internal-Token");

            if (authorizationHeader != null && !authorizationHeader.isBlank()) {

                requestTemplate.header(
                        "X-Internal-Token",
                        authorizationHeader);
            }

            String accountId = request.getHeader("X-Account-Id");
            if (accountId != null) {
                requestTemplate.header("X-Account-Id", accountId);
            }
            String userId = request.getHeader("X-Email");
            if (userId != null) {
                requestTemplate.header("X-Email", userId);
            }

            String role = request.getHeader("X-User-Role");
            if (role != null) {
                requestTemplate.header("X-User-Role", role);
            }
        };
    }
}
