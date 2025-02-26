package com.sourcery.gymapp.backend.userProfile.mapper;

import com.sourcery.gymapp.backend.events.LikeNotificationEvent;
import com.sourcery.gymapp.backend.userProfile.dto.LikeNotificationDto;
import com.sourcery.gymapp.backend.userProfile.model.LikeNotification;
import org.springframework.stereotype.Component;

@Component
public class LikeNotificationMapper {
    public LikeNotificationDto toDto(LikeNotification likeNotification) {
        return new LikeNotificationDto(
                likeNotification.getOwnerId(),
                likeNotification.getRoutineId(),
                likeNotification.getLikesCount(),
                likeNotification.getCreatedAt()
        );
    }

    public LikeNotification toEntity(LikeNotificationEvent event) {
        LikeNotification entity =  new LikeNotification();
        entity.setLikesCount(event.likesCount());
        entity.setRoutineId(event.routineId());
        entity.setOwnerId(event.ownerId());
        return entity;
    }
}
