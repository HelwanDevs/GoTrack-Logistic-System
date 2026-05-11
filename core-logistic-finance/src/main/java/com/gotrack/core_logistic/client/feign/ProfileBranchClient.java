package com.gotrack.core_logistic.client.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.gotrack.core_logistic.model.dto.ProfileResponse;



@FeignClient(name = "user-branch-service")
public interface ProfileBranchClient {

    @GetMapping("/api/users/profiles/account/{accountId}")
    ProfileResponse getProfileByAccountId(@PathVariable("accountId") String accountId);

    @GetMapping("/api/users/profiles/{id}")
    ProfileResponse getProfileById(@PathVariable("id") Long Id);


}
