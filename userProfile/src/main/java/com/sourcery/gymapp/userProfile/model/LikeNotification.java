package com.sourcery.gymapp.userProfile.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "like_notification")
public class LikeNotification extends BaseEntity {

    @Column(nullable = false)
    private UUID ownerId;

    @Column(nullable = false)
    private UUID routineId;

    @Column(nullable = false)
    private String routineTitle;

    @Column(nullable = false)
    private int likesCount;
}
