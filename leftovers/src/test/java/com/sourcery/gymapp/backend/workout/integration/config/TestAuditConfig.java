package com.sourcery.gymapp.workout.integration.config;

import java.util.UUID;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

@TestConfiguration
public class TestAuditConfig {
    private static final UUID TEST_SCOPED_UUID =
            UUID.fromString("00000000-0000-0000-0000-000000000001");
    @Bean
    public AuditorAware<UUID> auditorProvider() {
        return () -> Optional.of(TEST_SCOPED_UUID);
    }
}
