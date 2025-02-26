package com.sourcery.gymapp.backend.userProfile.service;

import com.sourcery.gymapp.backend.events.LikeNotificationEvent;
import com.sourcery.gymapp.backend.userProfile.mapper.LikeNotificationMapper;
import com.sourcery.gymapp.backend.userProfile.model.LikeNotification;
import com.sourcery.gymapp.backend.userProfile.repository.LikeNotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeNotificationService {
    private final LikeNotificationRepository likeNotificationRepository;
    private final LikeNotificationMapper likeNotificationMapper;

    @Value("${spring.kafka.aggregation.like-notification.interval-minutes}")
    private int aggregationInterval;

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
