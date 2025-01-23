package com.sourcery.gymapp.backend.userProfile.service;

import java.util.UUID;

import com.sourcery.gymapp.backend.globalconfig.AuditorConfig;
import com.sourcery.gymapp.backend.userProfile.exception.UserNotAuthenticatedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
public class ProfileCurrentUserService {

    public UUID getCurrentUserId() {
        if (AuditorConfig.AuditorAwareImpl.getKafkaProcessing()) {
            return AuditorConfig.AuditorAwareImpl.getSystemUserUUID();
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            return UUID.fromString(jwt.getClaimAsString("userId"));
        }

        throw new UserNotAuthenticatedException();
    }
}
