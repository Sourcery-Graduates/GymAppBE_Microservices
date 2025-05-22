package com.sourcery.gymapp.workout.config;

import java.util.UUID;
import org.springframework.stereotype.Service;

/**
 * Service for retrieving current user information from JWT token.
 * Integrates with Spring Security to access authentication context.
 */
@Service
public class CurrentUserService {

    /**
     * Retrieves the current user's ID from JWT token.
     * 
     * @return UUID of the authenticated user
     * @throws IllegalStateException if user is not authenticated or JWT token is invalid
     */
    public UUID getCurrentUserId() {
        return UUID.randomUUID(); //TODO: Current user id placeholder
    }
}
