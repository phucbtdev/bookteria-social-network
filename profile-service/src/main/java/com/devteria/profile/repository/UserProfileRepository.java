package com.devteria.profile.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devteria.profile.enity.UserProfile;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, String> {
    UserProfile findByUserId(String userId);
}
