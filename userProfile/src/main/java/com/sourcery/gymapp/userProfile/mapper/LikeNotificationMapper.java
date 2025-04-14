package com.sourcery.gymapp.userProfile.mapper;

import com.sourcery.gymapp.userProfile.event.LikeNotificationEvent;
import com.sourcery.gymapp.userProfile.dto.LikeNotificationDto;
import com.sourcery.gymapp.userProfile.model.LikeNotification;
import org.springframework.stereotype.Component;

@Component
public class LikeNotificationMapper {
    public LikeNotificationDto toDto(LikeNotification likeNotification) {
        return new LikeNotificationDto(
                likeNotification.getOwnerId(),
                likeNotification.getRoutineId(),
                likeNotification.getLikesCount(),
                likeNotification.getRoutineTitle(),
                likeNotification.getCreatedAt()
        );
    }

    public LikeNotification toEntity(LikeNotificationEvent event) {
        LikeNotification entity = new LikeNotification();
        entity.setLikesCount(event.likesCount());
        entity.setRoutineId(event.routineId());
        entity.setOwnerId(event.ownerId());
        entity.setRoutineTitle(event.routineName());
        return entity;
    }
}
