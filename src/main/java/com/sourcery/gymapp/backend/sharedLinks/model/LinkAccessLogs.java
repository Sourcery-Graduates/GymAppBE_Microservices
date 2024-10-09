package com.sourcery.gymapp.backend.sharedLinks.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "link_access_logs", schema = "shared_links")
public class LinkAccessLogs extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "link_id")
    private Link link;

    @NotNull
    private LocalDateTime accessedAt;

    @NotNull
    private UUID accessedByUserId;
}
