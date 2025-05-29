package com.sourcery.gymapp.workout.service;

import java.util.UUID;

import com.sourcery.gymapp.workout.exception.WorkoutRuntimeException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class CurrentUserService {

    public UUID getCurrentUserId() {
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();

        String userIdHeader = request.getHeader("X-User-Id");

        if (userIdHeader == null) {
            throw new WorkoutRuntimeException("Missing X-User-Id Header");
        }
        return UUID.fromString(userIdHeader);
    }
}
