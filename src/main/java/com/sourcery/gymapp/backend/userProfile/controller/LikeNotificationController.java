package com.sourcery.gymapp.backend.userProfile.controller;

import com.sourcery.gymapp.backend.userProfile.dto.LikeNotificationPageDto;
import com.sourcery.gymapp.backend.userProfile.service.LikeNotificationService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user-profile/like-notifications")
@RequiredArgsConstructor
public class LikeNotificationController {
    private final LikeNotificationService likeNotificationService;

    @GetMapping
    public LikeNotificationPageDto getLikeNotifications(
            @ParameterObject @PageableDefault(size = 10, sort = "createdAt") Pageable pageable
    ) {
        return likeNotificationService.getLikeNotifications(pageable);
    }
}
