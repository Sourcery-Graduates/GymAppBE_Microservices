package com.sourcery.gymapp.backend.email.consumer;


import com.fasterxml.jackson.databind.ObjectMapper;
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

    @KafkaListener(topics = {"${spring.kafka.topics.email-send}"}, groupId = "email-listener-group")
    public void onMessage(ConsumerRecord<UUID, String> record) {
        try {
            KafkaProcessingContext.enableKafkaProcessing();

            var email = objectMapper.readValue(record.value(), EmailSendEvent.class);
            emailService.sendEmail(email);
            log.info("Registration event email processed: {}", record.key());
        } catch (Exception e) {
            log.error("Error processing registration email event: {}", e.getMessage(), e);
        } finally {
            KafkaProcessingContext.disableKafkaProcessing();
        }
    }
}