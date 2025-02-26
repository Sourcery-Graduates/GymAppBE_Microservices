package com.sourcery.gymapp.backend.userProfile.repository;

import com.sourcery.gymapp.backend.userProfile.model.LikeNotification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface LikeNotificationRepository extends JpaRepository<LikeNotification, UUID> {

    Optional<LikeNotification> findFirstByOwnerIdAndRoutineIdAndCreatedAtAfter(UUID ownerId, UUID routineId, LocalDateTime createdAt);

    Page<LikeNotification> findByOwnerIdOrderByCreatedAtDesc(UUID ownerId, Pageable pageable);
}
