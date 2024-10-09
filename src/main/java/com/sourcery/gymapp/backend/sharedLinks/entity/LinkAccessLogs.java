package com.sourcery.gymapp.backend.sharedLinks.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "link_access_logs")
public class LinkAccessLogs extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "link_id", referencedColumnName = "id", nullable = false)
    private SharedLinks sharedLink;

    private LocalDateTime accessedAt;
    private UUID accessedByUserId;
}
