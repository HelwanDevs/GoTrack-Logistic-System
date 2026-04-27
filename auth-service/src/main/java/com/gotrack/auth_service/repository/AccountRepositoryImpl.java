package com.gotrack.auth_service.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.gotrack.auth_service.entity.Account;
import com.gotrack.auth_service.enums.Role;
import java.util.List;

@Repository
public class AccountRepositoryImpl implements AccountRepositoryCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Page<Account> findByDynamicFilters(
            Role role,
            String email,
            Boolean includeDeleted,
            Pageable pageable) {

        // Build dynamic criteria
        Criteria criteria = new Criteria();

        if (includeDeleted == null || !includeDeleted) {
            criteria = criteria.and("deleted").ne(true); //  deleted !== true
        }

        if (role != null) {
            criteria = criteria.and("role").is(role);
        }

        if (email != null && !email.isEmpty()) {
            criteria = criteria.and("email").regex(email, "i");
        }


        Query query = new Query(criteria).with(pageable);

        long total = mongoTemplate.count(new Query(criteria), Account.class);

        List<Account> accounts = mongoTemplate.find(query, Account.class);

        return new PageImpl<Account>(accounts, pageable, total);
    }
}
