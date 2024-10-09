package com.sourcery.gymapp.backend.sharedLinks.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "links", schema = "shared_links")
public class Link extends BaseEntity {

    private UUID userId;
    private UUID routineId;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "link", cascade = CascadeType.ALL)
    private List<LinkAccessLogs> linkAccessLogs;

    @NotNull
    private String link;
    private boolean isActive;

    @NotNull
    private LocalDateTime expiresAt;
}