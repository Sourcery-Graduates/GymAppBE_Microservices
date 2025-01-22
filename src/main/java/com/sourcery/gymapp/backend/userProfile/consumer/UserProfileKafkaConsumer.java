package com.sourcery.gymapp.backend.userProfile.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sourcery.gymapp.backend.common.domain.RegistrationEvent;
import com.sourcery.gymapp.backend.globalconfig.AuditorConfig;
import com.sourcery.gymapp.backend.userProfile.service.UserProfileService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
@AllArgsConstructor
public class UserProfileKafkaConsumer {
    private final UserProfileService userProfileService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = {"${spring.kafka.topics.account-register}"}, groupId = "user-profile-listener-group")
    public void onMessage(ConsumerRecord<UUID, String> record) throws JsonProcessingException {
        try {
            AuditorConfig.AuditorAwareImpl.enableKafkaProcessing();

            var data = objectMapper.readValue(record.value(), RegistrationEvent.class);
            userProfileService.createUserProfileAfterRegistration(data.user());
            log.info("Registration event received: " + data.eventID());

        } finally {
            AuditorConfig.AuditorAwareImpl.disableKafkaProcessing();
        }
    }
}
