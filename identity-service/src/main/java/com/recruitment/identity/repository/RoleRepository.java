package com.recruitment.identity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.recruitment.identity.entity.Roles;

@Repository
public interface RoleRepository extends JpaRepository<Roles, String> {}
