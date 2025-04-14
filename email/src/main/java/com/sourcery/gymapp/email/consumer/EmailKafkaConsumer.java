package com.sourcery.gymapp.email.consumer;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sourcery.gymapp.email.service.EmailService;
import com.sourcery.gymapp.globalconfig.KafkaProcessingContext;
import com.sourcery.gymapp.email.event.EmailSendEvent;
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

    @KafkaListener(topics = {"${spring.kafka.topics.email-send}", "${spring.kafka.topics.email-retry}"}, groupId = "email-listener-group")
    public void onMessage(ConsumerRecord<UUID, String> record) {
        try {
            KafkaProcessingContext.enableKafkaProcessing();

            var email = objectMapper.readValue(record.value(), EmailSendEvent.class);
            emailService.sendEmail(email);
            log.info("Email event processed from topic: {}, key: {}", record.topic(), record.key());

        } catch (Exception e) {
            log.error("Error processing email event from topic: {}, key: {}, message: {}", record.topic(), record.key(), e.getMessage(), e);
        } finally {
            KafkaProcessingContext.disableKafkaProcessing();
        }
    }
}