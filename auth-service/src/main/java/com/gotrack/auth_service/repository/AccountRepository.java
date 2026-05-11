package com.gotrack.auth_service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.gotrack.auth_service.entity.Account;
import java.util.Optional;


@Repository
public interface AccountRepository extends MongoRepository<Account, String>, AccountRepositoryCustom {
    boolean existsByEmail(String email);

    Optional<Account> findByEmail(String email);

    Optional<Account> findById(String id);

}

