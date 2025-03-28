package com.sourcery.gymapp.backend.authentication.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sourcery.gymapp.backend.authentication.exception.AuthenticationRuntimeException;
import com.sourcery.gymapp.backend.events.RegistrationEvent;
import com.sourcery.gymapp.backend.events.EmailSendEvent;
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
    private String accountRegisterTopicName;

    @Value("${spring.kafka.topics.email-send}")
    private String emailTopicName;

    private final KafkaTemplate<UUID, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public AuthKafkaProducer(KafkaTemplate<UUID, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public CompletableFuture<SendResult<UUID, String>> sendRegistrationEvent(RegistrationEvent event) {
        var key = event.userId();
        String value = null;
        try {
            value = objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            throw new AuthenticationRuntimeException("Couldn't convert to JSON at Kafka Producer" + e.getMessage());
        }

        var future = kafkaTemplate.send(accountRegisterTopicName, key, value);

        String finalValue = value;
        return future.whenComplete((result, error) -> {
            if (error != null) {
                log.error("Error sending library event: {}", error.getMessage(), error);
            } else {
                log.info("Successfully sent registration event: \n key: {}\n value: {}", key, finalValue);
            }
        });
    }

    public CompletableFuture<SendResult<UUID, String>> sendEmailEvent(EmailSendEvent email, UUID key) {

        String value = null;
        try {
            value = objectMapper.writeValueAsString(email);
        } catch (JsonProcessingException e) {
            throw new AuthenticationRuntimeException("Couldn't convert to JSON at Kafka Producer" + e.getMessage());
        }

        var future = kafkaTemplate.send(emailTopicName, key, value);

        String finalValue = value;
        return future.whenComplete((result, error) -> {
            if (error != null) {
                log.error("Error sending library event: {}", error.getMessage(), error);
            } else {
                log.info("Successfully sent registration email event: \n key: {}\n value: {}", key, finalValue);
            }
        });
    }
}
