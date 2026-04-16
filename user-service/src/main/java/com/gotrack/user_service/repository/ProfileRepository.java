package com.gotrack.user_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gotrack.user_service.model.Profile;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

}