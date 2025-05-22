package com.sourcery.gymapp.userProfile.service;

import java.util.UUID;

import com.sourcery.gymapp.userProfile.config.AuditorConfig;
import com.sourcery.gymapp.userProfile.config.KafkaProcessingContext;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {

    public UUID getCurrentUserId() {
        if (KafkaProcessingContext.isKafkaProcessing()) {
            return AuditorConfig.AuditorAwareImpl.getSystemUserUUID();
        }

        return UUID.randomUUID(); //TODO: Current user id placeholder
    }
}
