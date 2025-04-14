package com.sourcery.gymapp.userProfile.dto;

import java.util.List;

public record LikeNotificationPageDto(
        int totalPages,
        long totalElements,
        List<LikeNotificationDto> data
) {
}
