package com.sourcery.gymapp.backend.authentication.service;

import com.sourcery.gymapp.backend.authentication.model.Permission;
import com.sourcery.gymapp.backend.authentication.repo.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final PermissionRepository permissionRepository;

    public Permission createPermission(Permission permission) {
        return permissionRepository.save(permission);
    }

    public Optional<Permission> getPermissionById(UUID id) {
        return permissionRepository.findById(id);
    }

    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }

    public void deletePermissionById(UUID id) {
        permissionRepository.deleteById(id);
    }
}
