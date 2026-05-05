package com.gotrack.user_service.repository;

import com.gotrack.user_service.domain.entity.ProfileEntity;
import com.gotrack.user_service.domain.enums.ProfileStatus;
import com.gotrack.user_service.domain.enums.ProfileType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<ProfileEntity, Long>,
        JpaSpecificationExecutor<ProfileEntity> { 

    Optional<ProfileEntity> findByAccountId(Long accountId);
    Optional<ProfileEntity> findByPhoneNumber(String phoneNumber);
    List<ProfileEntity> findByType(ProfileType type);
    List<ProfileEntity> findByStatus(ProfileStatus status);
    List<ProfileEntity> findByBranchId(Long branchId);

}