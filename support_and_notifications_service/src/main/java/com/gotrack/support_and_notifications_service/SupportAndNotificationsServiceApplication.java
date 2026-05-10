package com.gotrack.support_and_notifications_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableFeignClients
public class SupportAndNotificationsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SupportAndNotificationsServiceApplication.class, args);
	}

}
