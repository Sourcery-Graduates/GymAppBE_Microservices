package com.sourcery.gymapp.backend.sharedLinks.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.OneToMany;
import jakarta.persistence.FetchType;
import jakarta.persistence.CascadeType;
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

    @NotNull
    private UUID userId;

    @NotNull
    private UUID routineId;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "link")
    private List<LinkAccessLogs> linkAccessLogs;

    @NotNull
    private String link;

    @NotNull
    private boolean isActive;

    @NotNull
    private LocalDateTime expiresAt;
}