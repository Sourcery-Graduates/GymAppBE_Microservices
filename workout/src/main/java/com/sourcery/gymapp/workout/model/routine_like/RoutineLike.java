package com.sourcery.gymapp.workout.model.routine_like;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "routine_like")
@IdClass(RoutineLikeId.class)
public class RoutineLike implements Serializable {

    @Id
    @Column(nullable = false)
    private UUID routineId;

    @Id
    @Column(nullable = false)
    private UUID userId;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;
}
