package com.sourcery.gymapp.backend.authentication.service;

import com.sourcery.gymapp.backend.authentication.converter.PermissionConverter;
import com.sourcery.gymapp.backend.authentication.dto.CreatePermissionDto;
import com.sourcery.gymapp.backend.authentication.dto.PermissionResponseDto;
import com.sourcery.gymapp.backend.authentication.repo.PermissionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final PermissionRepository permissionRepository;
    private final PermissionConverter permissionConverter;

    public PermissionResponseDto createPermission(CreatePermissionDto dto) {
        var entity = permissionConverter.toEntity(dto);
        permissionRepository.save(entity);
        return permissionConverter.toResponseDTO(entity);
    }

    public PermissionResponseDto getPermissionById(UUID id) {
        var entity = permissionRepository.findById(id);
        if(entity.isPresent()) {
            return permissionConverter.toResponseDTO(entity.get());
        } else {
            throw new EntityNotFoundException("No entity was found with that Id");
        }

    }

    public List<PermissionResponseDto> getAllPermissions() {
        return permissionRepository
                .findAll()
                .stream()
                .map(permissionConverter::toResponseDTO)
                .toList();
    }

    public void deletePermissionById(UUID id) {
        permissionRepository.deleteById(id);
    }
}
