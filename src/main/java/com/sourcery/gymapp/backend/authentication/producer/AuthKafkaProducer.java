package com.sourcery.gymapp.backend.authentication.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sourcery.gymapp.backend.events.RegistrationEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class AuthKafkaProducer {
    @Value("${spring.kafka.topics.account-register}")
    private String topicName;
    private final KafkaTemplate<UUID, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public AuthKafkaProducer(KafkaTemplate<UUID, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public CompletableFuture<SendResult<UUID, String>> sendRegistrationEvent(RegistrationEvent event) {
        var key = event.eventId();
        String value = null;
        try {
            value = objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        var future = kafkaTemplate.send(topicName, key, value);
        String finalValue = value;
        return future.whenComplete((result, error) -> {
            if (error != null) {
                handleException(key, finalValue, error);
            } else {
                handleSuccess(key, finalValue, result);
            }
        });
    }

    private void handleSuccess(UUID key, String value, SendResult<UUID, String> result) {
        log.info("Successfully sent registration event: \n key: {}\n value: {}\n", key, value);
    }

    private void handleException(UUID key, String value, Throwable error) {
        log.error("Error sending library event: {}", error.getMessage(), error);
    }
}
