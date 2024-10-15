package com.sourcery.gymapp.backend.authentication.repo;

import com.sourcery.gymapp.backend.authentication.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PermissionRepository extends JpaRepository<Permission, UUID> {
}