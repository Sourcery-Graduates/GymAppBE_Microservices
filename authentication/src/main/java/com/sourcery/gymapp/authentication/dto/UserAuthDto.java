package com.sourcery.gymapp.authentication.dto;

import com.sourcery.gymapp.authentication.model.RolePermission;
import java.util.Map;
import java.util.Set;

public record UserAuthDto(
        String token,
        String username,
        String email,
        Set<String> roles,
        Map<String, RolePermission.Level> permissions
) {}
