package com.sourcery.gymapp.workout.service;

import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {

    public UUID getCurrentUserId() {
        return UUID.randomUUID(); //TODO: Current user id placeholder
    }
}
