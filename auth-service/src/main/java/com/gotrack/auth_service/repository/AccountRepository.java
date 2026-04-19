package com.gotrack.auth_service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.gotrack.auth_service.entity.Account;

@Repository
public interface AccountRepository extends MongoRepository<Account, String> {
}
