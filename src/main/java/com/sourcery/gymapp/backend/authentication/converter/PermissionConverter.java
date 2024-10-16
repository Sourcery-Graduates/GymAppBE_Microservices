package com.sourcery.gymapp.backend.authentication.converter;

import com.sourcery.gymapp.backend.authentication.dto.CreatePermissionDto;
import com.sourcery.gymapp.backend.authentication.dto.PermissionResponseDto;
import com.sourcery.gymapp.backend.authentication.model.Permission;
import org.springframework.stereotype.Component;

@Component
public class PermissionConverter {
    public Permission toEntity(CreatePermissionDto dto) {
        Permission permission = new Permission();
        permission.setDescription(dto.description());
        permission.setName(dto.name());
        return permission;
    }

    public PermissionResponseDto toResponseDTO(Permission entity) {
        return new PermissionResponseDto(
                entity.getId(),
                entity.getCreatedAt(),
                entity.getModifiedAt(),
                entity.getCreatedBy(),
                entity.getModifiedBy(),
                entity.getName(),
                entity.getDescription()
        );
    }
}
