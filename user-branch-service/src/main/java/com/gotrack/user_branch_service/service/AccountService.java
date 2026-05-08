package com.gotrack.user_branch_service.service;

import java.util.Map;


public interface AccountService { 
    Map<String, Object> getAccountById(String accountId);
    boolean isAccountValid(String accountId);
    boolean isSuperAdmin(String accountId);
}