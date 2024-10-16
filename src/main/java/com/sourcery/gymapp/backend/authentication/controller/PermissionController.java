package com.sourcery.gymapp.backend.authentication.controller;

import com.sourcery.gymapp.backend.authentication.dto.CreatePermissionDto;
import com.sourcery.gymapp.backend.authentication.dto.PermissionResponseDto;
import com.sourcery.gymapp.backend.authentication.model.Permission;
import com.sourcery.gymapp.backend.authentication.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    @PostMapping
    public ResponseEntity<PermissionResponseDto> createPermission(@RequestBody CreatePermissionDto dto) {
        PermissionResponseDto createdPermission = permissionService.createPermission(dto);
        return ResponseEntity.ok(createdPermission);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PermissionResponseDto> getPermissionById(@PathVariable UUID id) {
        var permission = permissionService.getPermissionById(id);
        return ResponseEntity.ok(permission);
    }

    @GetMapping
    public ResponseEntity<List<PermissionResponseDto>> getAllPermissions() {
        var permissions = permissionService.getAllPermissions();
        return ResponseEntity.ok(permissions);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePermission(@PathVariable UUID id) {
        permissionService.deletePermissionById(id);
        return ResponseEntity.noContent().build();
    }
}
