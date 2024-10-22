package com.sourcery.gymapp.backend.globalconfig;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;
import java.util.UUID;

@Configuration
@RequiredArgsConstructor
public class AuditorConfig {

    @Bean
    public AuditorAwareImpl auditorProvider(CurrentUserService currentUserService) {
        return new AuditorAwareImpl(currentUserService);
    }

    @RequiredArgsConstructor
    public static class AuditorAwareImpl implements AuditorAware<UUID> {
        private final CurrentUserService currentUserService;

        @Override
        @NonNull
        public Optional<UUID> getCurrentAuditor() {
            return Optional.of(currentUserService.getCurrentUserId());
        }
    }
}
