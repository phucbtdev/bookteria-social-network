package com.recruitment.identity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.recruitment.identity.entity.Permissions;

@Repository
public interface PermissionRepository extends JpaRepository<Permissions, String> {}
