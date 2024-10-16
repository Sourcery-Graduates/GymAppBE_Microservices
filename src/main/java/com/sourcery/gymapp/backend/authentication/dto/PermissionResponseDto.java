package com.sourcery.gymapp.backend.authentication.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public record PermissionResponseDto(
        UUID id,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt,
        UUID createdBy,
        UUID modifiedBy,
        String name,
        String description
) implements Serializable {
}