package com.sourcery.gymapp.userProfile.service;

import java.util.UUID;

import com.sourcery.gymapp.userProfile.config.AuditorConfig;
import com.sourcery.gymapp.userProfile.config.KafkaProcessingContext;
import com.sourcery.gymapp.userProfile.exception.UserNotAuthenticatedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
public class ProfileCurrentUserService {

    public UUID getCurrentUserId() {
        if (KafkaProcessingContext.isKafkaProcessing()) {
            return AuditorConfig.AuditorAwareImpl.getSystemUserUUID();
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            return UUID.fromString(jwt.getClaimAsString("userId"));
        }

        throw new UserNotAuthenticatedException();
    }
}
