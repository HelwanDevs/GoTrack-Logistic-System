package com.gotrack.auth_service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.gotrack.auth_service.entity.Account;
import com.gotrack.auth_service.enums.Role;

public interface AccountRepositoryCustom {
    

    Page<Account> findByDynamicFilters(
        Role role,
        String email,
        Boolean includeDeleted,
        Pageable pageable
    );
}
