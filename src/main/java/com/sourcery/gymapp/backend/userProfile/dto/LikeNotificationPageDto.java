package com.sourcery.gymapp.backend.userProfile.dto;

import java.util.List;

public record LikeNotificationPageDto(
        int totalPages,
        long totalElements,
        List<LikeNotificationDto> data
) {
}
