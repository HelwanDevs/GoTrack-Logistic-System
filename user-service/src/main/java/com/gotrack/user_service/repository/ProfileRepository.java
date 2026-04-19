package com.gotrack.user_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gotrack.user_service.domain.entity.ProfileEntity;
import com.gotrack.user_service.domain.entity.ProfileStatus;
import com.gotrack.user_service.domain.entity.ProfileType;

@Repository
public interface ProfileRepository extends JpaRepository<ProfileEntity, Long> {
    Optional<ProfileEntity> findByAccountId(Long accountId);

    List<ProfileEntity> findByType(ProfileType type);

    List<ProfileEntity> findByStatus(ProfileStatus status);

    List<ProfileEntity> findByBranchId(Long branchId);
}