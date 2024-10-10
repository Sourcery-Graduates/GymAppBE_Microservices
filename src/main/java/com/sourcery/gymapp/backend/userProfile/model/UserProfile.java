package com.sourcery.gymapp.backend.userProfile.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "profiles", schema = "user_profiles")
public class UserProfile extends BaseEntity {
    @Column(nullable = false, unique = true)
    private UUID userId;

    @Column(length = 32, nullable = false, unique = true)
    private String username;

    @Column(length = 64)
    private String firstName;

    @Column(length = 64)
    private String lastName;

    private String bio;

    private String avatarUrl;

    @Column(length = 128)
    private String location;

    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> settings;
}
