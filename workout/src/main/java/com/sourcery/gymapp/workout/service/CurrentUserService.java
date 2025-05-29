package com.sourcery.gymapp.workout.service;

import java.util.UUID;

import com.sourcery.gymapp.workout.exception.UserIdHeaderNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class CurrentUserService {

    public UUID getCurrentUserId() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new IllegalStateException("No request context available");
        }
        HttpServletRequest request = attributes.getRequest();
        String userIdHeader = request.getHeader("X-User-Id");
        if (userIdHeader == null) {
            throw new UserIdHeaderNotFoundException();
        }
        return UUID.fromString(userIdHeader);
    }
}
