package com.sourcery.gymapp.backend.userProfile.dto;

import com.sourcery.gymapp.backend.userProfile.model.LikeNotification;

import java.util.List;

public record LikeNotificationPageDto(
        int totalPages,
        long totalElements,
        List<LikeNotificationDto> data
) {
}
