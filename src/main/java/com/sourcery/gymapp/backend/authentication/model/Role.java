package com.sourcery.gymapp.backend.authentication.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "roles", schema = "user_auth")
public class Role extends BaseEntity {

    @Column(length = 64, nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "role")
    private Set<RolePermission> rolePermissions;
}
