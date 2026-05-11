package com.gotrack.user_branch_service.service.imp;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gotrack.user_branch_service.client.feign.AuthClient;

@Service
public class AccountServiceImp {

    @Autowired
    private AuthClient authClient;

    public Map<String, Object> getAccountById(String accountId) {
        return authClient.getAccountById(accountId);
    }

    public boolean isAccountValid(String accountId) {
        try {
            Map<String, Object> account = authClient.getAccountById(accountId);
            return account != null
                    && !"true".equals(String.valueOf(account.get("deleted")));
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isSuperAdmin(String accountId) {
        try {
            return authClient.isSuperAdmin(accountId);
        } catch (Exception e) {
            return false;
        }
    }

}
