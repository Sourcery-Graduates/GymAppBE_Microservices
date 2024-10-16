package com.sourcery.gymapp.backend.globalConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;
import java.util.UUID;

@Configuration
public class AuditorConfig {

    @Bean
    public AuditorAware<UUID> auditorProvider() {
        return new AuditorAwareImpl();
    }

    public static class AuditorAwareImpl implements AuditorAware<UUID> {
        @Override
        public Optional<UUID> getCurrentAuditor() {
            return Optional.of(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        }
    }
}
