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

/**
 * Configuration class for JPA auditing that provides user tracking functionality.
 * Implements custom auditor awareness to handle both authenticated users and system operations.
 */
@Configuration
@RequiredArgsConstructor
public class AuditorConfig {

    /**
     * Creates an AuditorAwareImpl bean for JPA auditing.
     *
     * @param currentUserService service to retrieve current user information
     * @return AuditorAwareImpl instance for auditing
     */
    @Bean
    public AuditorAwareImpl auditorProvider(CurrentUserService currentUserService) {
        return new AuditorAwareImpl(currentUserService);
    }

    /**
     * Implementation of AuditorAware interface that provides user IDs for JPA auditing.
     * Handles special cases like user registration where system user ID is used.
     */
    @RequiredArgsConstructor
    public static class AuditorAwareImpl implements AuditorAware<UUID> {
        private final CurrentUserService currentUserService;
        private static final UUID SYSTEM_USER_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");
        private static final String registrationPath = "/api/auth/register";

        /**
         * Determines the user ID to be used for auditing.
         * Returns system user ID for registration endpoint, otherwise returns current user ID.
         *
         * @return Optional containing either system user ID or current user ID
         */
        @Override
        @NonNull
        public Optional<UUID> getCurrentAuditor() {
            if (isRegistrationEndpoint()) {
                return Optional.of(SYSTEM_USER_UUID);
            }
            return Optional.of(currentUserService.getCurrentUserId());
        }

        /**
         * Checks if the current request is for user registration.
         *
         * @return true if current request is for registration endpoint, false otherwise
         */
        private boolean isRegistrationEndpoint() {
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            if (requestAttributes instanceof ServletRequestAttributes) {
                String requestUri = ((ServletRequestAttributes) requestAttributes).getRequest().getRequestURI();
                return registrationPath.equals(requestUri);
            }
            return false;
        }
    }
}
