package com.sourcery.gymapp.backend.authentication.service;

import com.sourcery.gymapp.backend.authentication.model.Permission;
import com.sourcery.gymapp.backend.authentication.repo.PermissionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PermissionService {

    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public Permission createPermission(Permission permission) {
        return permissionRepository.save(permission);
    }

    public Optional<Permission> getPermissionById(UUID id) {
        return permissionRepository.findById(id);
    }

    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }
}
