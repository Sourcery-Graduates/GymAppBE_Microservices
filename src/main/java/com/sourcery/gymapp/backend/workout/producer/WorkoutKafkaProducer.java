package com.sourcery.gymapp.backend.workout.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sourcery.gymapp.backend.events.RoutineLikeEvent;
import com.sourcery.gymapp.backend.workout.exception.WorkoutRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class WorkoutKafkaProducer {

    @Value("${spring.kafka.topics.likes-events}")
    private String likesEventsTopicName;

    private KafkaTemplate<UUID, String> kafkaTemplate;
    private ObjectMapper objectMapper;

    public WorkoutKafkaProducer(KafkaTemplate<UUID, String> kafkaTemplate, ObjectMapper mapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = mapper;
    }

    public CompletableFuture<SendResult<UUID, String>> sendRoutineLikeEvent(RoutineLikeEvent event) {
        UUID key = event.routineId();

        String value = null;
        try {
            value = objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            throw new WorkoutRuntimeException("Couldn't convert to JSON at Kafka Producer" + e.getMessage());
        }

        var future = kafkaTemplate.send(likesEventsTopicName, key, value);

        String finalValue = value;
        return future.whenComplete((result, error) -> {
            if (error != null) {
                log.error("Error sending routine like event: {}", error.getMessage(), error);
            } else {
                log.info("Successfully sent routine like event: \n key: {}\n value: {}", key, finalValue);
            }
        });
    }
}
