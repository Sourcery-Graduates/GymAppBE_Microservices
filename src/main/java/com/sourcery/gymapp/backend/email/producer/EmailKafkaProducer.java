package com.sourcery.gymapp.backend.email.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class EmailKafkaProducer {

    @Value("${spring.kafka.topics.email-retry}")
    private String EMAIL_RETRY_TOPIC;

    private KafkaTemplate<UUID, String> kafkaTemplate;
    private ObjectMapper objectMapper;

    public EmailKafkaProducer(KafkaTemplate<UUID, String> kafkaTemplate, ObjectMapper mapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = mapper;
    }

    public CompletableFuture<SendResult<UUID, String>> retryEmail(UUID key, EmailSendEvent event) {
        String value = null;
        try {
            value = objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Couldn't convert to JSON at Kafka Producer" + e.getMessage());
        }

        var future = kafkaTemplate.send(EMAIL_RETRY_TOPIC, key, value);

        String finalValue = value;
        return future.whenComplete((result, error) -> {
            if (error != null) {
                log.error("Error sending email retry event: {}", error.getMessage(), error);
            } else {
                log.info("Successfully sent email retry event: \n key: {}\n value: {}", key, finalValue);
            }
        });
    }
}