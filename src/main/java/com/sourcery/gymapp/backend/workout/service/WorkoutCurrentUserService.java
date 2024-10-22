package com.sourcery.gymapp.backend.workout.service;

import com.sourcery.gymapp.backend.workout.exception.JwtIllegalStateException;
import com.sourcery.gymapp.backend.workout.exception.UserNotAuthenticatedException;
import java.util.UUID;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
public class WorkoutCurrentUserService {

    public UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            UUID userId = jwt.getClaim("userId");
            if (userId == null) {
                throw new JwtIllegalStateException("There is no userId in JWT");
            }
            return userId;
        }
        throw new UserNotAuthenticatedException();
    }
}
