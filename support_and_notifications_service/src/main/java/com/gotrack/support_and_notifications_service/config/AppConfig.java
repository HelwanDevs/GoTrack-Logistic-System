package com.gotrack.support_and_notifications_service.config;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ApplicationEventPublisher publisher(ApplicationEventPublisher publisher) {
        return publisher;
    }
}