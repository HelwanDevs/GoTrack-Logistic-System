package com.gotrack.user_branch_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class UserBranchServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserBranchServiceApplication.class, args);
	}

}
