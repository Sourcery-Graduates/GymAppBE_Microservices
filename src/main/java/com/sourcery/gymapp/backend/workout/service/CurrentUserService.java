package com.sourcery.gymapp.backend.workout.service;

import com.sourcery.gymapp.backend.workout.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CurrentUserService {
    private final AuditorAware<UUID> auditorAware;

    public UUID getCurrentUserId() {
        return auditorAware.getCurrentAuditor().orElseThrow(
                UserNotFoundException::new);
    }
}
