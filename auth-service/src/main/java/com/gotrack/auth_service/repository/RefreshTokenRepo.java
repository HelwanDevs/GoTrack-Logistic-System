package com.gotrack.auth_service.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.gotrack.auth_service.entity.RefreshToken;
import java.util.List;

public interface RefreshTokenRepo extends MongoRepository<RefreshToken, String> {

    void deleteByEmail(String email);

    Optional<RefreshToken> findByToken(String token);

    void deleteByToken(String token);

    Optional<RefreshToken> findByEmail(String email);
}