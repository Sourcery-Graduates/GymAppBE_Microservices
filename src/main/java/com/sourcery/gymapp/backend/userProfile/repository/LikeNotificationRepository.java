package com.sourcery.gymapp.backend.userProfile.repository;

import com.sourcery.gymapp.backend.userProfile.model.LikeNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LikeNotificationRepository extends JpaRepository<LikeNotification, UUID> {
}
