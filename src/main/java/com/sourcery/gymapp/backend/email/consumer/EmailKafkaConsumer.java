package com.sourcery.gymapp.backend.email.consumer;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sourcery.gymapp.backend.email.mapper.EmailMapper;
import com.sourcery.gymapp.backend.email.producer.EmailKafkaProducer;
import com.sourcery.gymapp.backend.email.service.EmailService;
import com.sourcery.gymapp.backend.globalconfig.KafkaProcessingContext;
import com.sourcery.gymapp.backend.events.EmailSendEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
@AllArgsConstructor
public class EmailKafkaConsumer {
    private final EmailService emailService;
    private final ObjectMapper objectMapper;
    private final EmailKafkaProducer emailKafkaProducer;
    private final EmailMapper emailMapper;
    private static final int MAX_RETRIES = 3;

    @KafkaListener(topics = {"${spring.kafka.topics.email-send}", "${spring.kafka.topics.email-retry}"}, groupId = "email-listener-group")
    public void onMessage(ConsumerRecord<UUID, String> record) {
        try {
            KafkaProcessingContext.enableKafkaProcessing();

            var email = objectMapper.readValue(record.value(), EmailSendEvent.class);
            var isSuccessful = emailService.sendEmail(email);

            if (!isSuccessful) {
                handleRetry(record.key(), email);
            } else {
                log.info("Email event processed from topic: {}, key: {}", record.topic(), record.key());
            }
        } catch (Exception e) {
            log.error("Error processing email event from topic: {}, key: {}, message: {}", record.topic(), record.key(), e.getMessage(), e);
        } finally {
            KafkaProcessingContext.disableKafkaProcessing();
        }
    }

    public void handleRetry(UUID eventId, EmailSendEvent email) {
        if (email.retryCount() < MAX_RETRIES) {
            log.warn("Retrying email for {} (attempt {}/{})", email.userEmail(), email.retryCount() + 1, MAX_RETRIES);
            EmailSendEvent retriedEmail = emailMapper.incrementRetryEvent(email);
            emailKafkaProducer.retryEmail(eventId, retriedEmail);
        } else {
            log.error("Error processing email event, Max retries reached: {}", email);
        }
    }
}