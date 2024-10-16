package com.sourcery.gymapp.backend.authentication.dto;

import java.io.Serializable;


public record CreatePermissionDto(
        String name,
        String description
) implements Serializable {}
