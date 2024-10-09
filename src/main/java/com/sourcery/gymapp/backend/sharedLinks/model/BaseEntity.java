package com.sourcery.gymapp.backend.sharedLinks.model;


import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    @Id
    @Column(updatable = false, nullable = false)
    protected UUID id;

    @Column(updatable = false, nullable = false)
    @CreatedDate
    protected LocalDateTime createdAt;

    @Column(nullable = false)
    @LastModifiedDate
    protected LocalDateTime modifiedAt;

    @Column(updatable = false, nullable = false)
    @CreatedBy
    protected UUID createdBy;

    @Column(nullable = false)
    @LastModifiedBy
    protected UUID modifiedBy;
}
