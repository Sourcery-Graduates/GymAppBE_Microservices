package com.sourcery.gymapp.backend.sharedLinks.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "shared_links")
public class SharedLinks extends BaseEntity {

    private UUID userId;
    private UUID routineId;

    @NotNull
    private String link;
    private boolean isActive;

    @NotNull
    private LocalDateTime expiresAt;
}