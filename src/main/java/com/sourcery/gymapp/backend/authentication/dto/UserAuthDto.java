package com.sourcery.gymapp.backend.authentication.dto;

import com.sourcery.gymapp.backend.authentication.model.RolePermission;
import java.util.Map;
import java.util.Set;

public record UserAuthDto(
        String token,
        String username,
        Set<String> roles,
        Map<String, RolePermission.Level> permissions
) {}
