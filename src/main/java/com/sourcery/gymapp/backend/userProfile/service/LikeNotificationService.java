package com.sourcery.gymapp.backend.userProfile.service;

import com.sourcery.gymapp.backend.events.LikeNotificationEvent;
import com.sourcery.gymapp.backend.userProfile.dto.LikeNotificationDto;
import com.sourcery.gymapp.backend.userProfile.dto.LikeNotificationPageDto;
import com.sourcery.gymapp.backend.userProfile.mapper.LikeNotificationMapper;
import com.sourcery.gymapp.backend.userProfile.model.LikeNotification;
import com.sourcery.gymapp.backend.userProfile.repository.LikeNotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service class for handling like notifications.
 * Manages persistence, retrieval, and aggregation of like notifications for user routines.
 */
@Service
@RequiredArgsConstructor
public class LikeNotificationService {
    private final LikeNotificationRepository likeNotificationRepository;
    private final LikeNotificationMapper likeNotificationMapper;
    private final ProfileCurrentUserService currentUserService;

    /**
     * Aggregation interval in minutes, retrieved from application properties.
     */
    @Value("${spring.kafka.aggregation.like-notification.interval-minutes}")
    private int aggregationInterval;

    /**
     * Retrieves a paginated list of like notifications for the current user.
     *
     * @param pageable the pagination and sorting information
     * @return a {@link LikeNotificationPageDto} containing the list of notifications,
     *         total pages, and total elements
     */
    public LikeNotificationPageDto getLikeNotifications(Pageable pageable) {
        UUID currentUserId = currentUserService.getCurrentUserId();

        Page<LikeNotification> page = likeNotificationRepository
                .findByOwnerIdOrderByCreatedAtDesc(currentUserId, pageable);

        List<LikeNotificationDto> notifications = page
                .getContent()
                .stream()
                .map(likeNotificationMapper::toDto)
                .toList();
        return new LikeNotificationPageDto(page.getTotalPages(), page.getTotalElements(), notifications);
    }

    /**
     * Handles the processing and storage of a new like notification event.
     * If a notification already exists within the aggregation time window, it updates the existing record;
     * otherwise, it creates a new notification entry.
     *
     * @param event the {@link LikeNotificationEvent} containing details of the like notification.
     */
    @Transactional
    public void uploadLikeNotifications(LikeNotificationEvent event) {
        LocalDateTime timeWindow = LocalDateTime.now().minusMinutes(aggregationInterval);
        Optional<LikeNotification> oldestNotification =
                likeNotificationRepository.findFirstByOwnerIdAndRoutineIdAndCreatedAtAfter(
                        event.ownerId(), event.routineId(), timeWindow
                );

        if (oldestNotification.isEmpty()) {
            LikeNotification newNotification = likeNotificationMapper.toEntity(event);
            likeNotificationRepository.save(newNotification);
        } else {
            LikeNotification notification = oldestNotification.get();
            notification.setLikesCount(event.likesCount());
            likeNotificationRepository.save(notification);
        }
    }

}
