package com.recruitment.identity.repository;

import java.util.Optional;

import com.recruitment.identity.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users, String> {
    boolean existsByEmail(String email);

    Optional<Users> findByEmail(String email);
}
