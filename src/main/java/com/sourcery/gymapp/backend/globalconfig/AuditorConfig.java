package com.sourcery.gymapp.backend.globalconfig;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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
        private static final UUID SYSTEM_USER_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");
        private static final String registrationPath = "/api/auth/register";

        @Override
        @NonNull
        public Optional<UUID> getCurrentAuditor() {
            if (isRegistrationEndpoint()) {
                return Optional.of(SYSTEM_USER_UUID);
            }
            return Optional.of(currentUserService.getCurrentUserId());
        }

        private boolean isRegistrationEndpoint() {
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            if (requestAttributes instanceof ServletRequestAttributes) {
                String requestUri = ((ServletRequestAttributes) requestAttributes).getRequest().getRequestURI();
                System.out.println(requestUri);
                return registrationPath.equals(requestUri);
            }
            return false;
        }
    }
}
