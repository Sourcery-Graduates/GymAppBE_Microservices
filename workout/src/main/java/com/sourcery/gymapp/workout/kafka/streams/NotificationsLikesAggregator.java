package com.sourcery.gymapp.workout.kafka.streams;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sourcery.gymapp.backend.events.LikeNotificationEvent;
import com.sourcery.gymapp.backend.events.RoutineLikeEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.kstream.Windowed;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.UUID;

/**
 * Kafka Streams processor for aggregating like events and publishing notifications.
 * This component listens to the input kafka topic, aggregates likes within a specified interval defined in minutes,
 * and produces summarized notifications to the output kafka topic.
 */
@Slf4j
@Component
public class NotificationsLikesAggregator {

    @Value("${spring.kafka.topics.likes-events}")
    private String INPUT_TOPIC;

    @Value("${spring.kafka.topics.likes-notifications}")
    private String OUTPUT_TOPIC;

    /**
     * Aggregation interval in minutes, retrieved from application properties.
     */
    @Value("${spring.kafka.aggregation.like-notification.interval-minutes}")
    private int aggregationInterval;

    private final ObjectMapper objectMapper;

    public NotificationsLikesAggregator() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }


    /**
     * Defines a Kafka Streams processing pipeline for aggregating routine likes.
     *
     * @param builder StreamsBuilder instance for constructing the processing topology.
     * @return A KStream processing pipeline producing like notification events.
     */
    @Bean
    public KStream<String, LikeNotificationEvent> notificationLikesStream(StreamsBuilder builder) {
        JsonSerde<RoutineLikeEvent> routineLikeSerde = new JsonSerde<>(RoutineLikeEvent.class);
        JsonSerde<LikeAggregation> aggregationSerde = new JsonSerde<>(LikeAggregation.class);
        JsonSerde<LikeNotificationEvent> notificationSerde = new JsonSerde<>(LikeNotificationEvent.class);

        KStream<String, RoutineLikeEvent> inputStream = builder
                .stream(INPUT_TOPIC, Consumed.with(Serdes.String(), routineLikeSerde));

        KTable<Windowed<String>, LikeAggregation> aggregatedLikes = inputStream
                .groupByKey()
                .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofMinutes(aggregationInterval)))
                .aggregate(
                        LikeAggregation::new,
                        (routineId, event, aggregation) -> aggregation.update(event),
                        Materialized.with(Serdes.String(), aggregationSerde)
                );

        KStream<String, LikeNotificationEvent> outputStream = aggregatedLikes
                .toStream((windowedKey, value) -> windowedKey.key())
                .mapValues((routineId, aggregation) -> new LikeNotificationEvent(
                        aggregation.ownerId,
                        UUID.fromString(routineId),
                        aggregation.routineName,
                        aggregation.likesCount
                ));

        outputStream.to(OUTPUT_TOPIC, Produced.with(Serdes.String(), notificationSerde));

        return outputStream;
    }

    /**
     * Data class for maintaining aggregated like counts within a time window.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class LikeAggregation {
        private UUID ownerId;
        private int likesCount = 0;
        private String routineName;

        /**
         * Updates the aggregation based on an incoming like event.
         *
         * @param event RoutineLikeEvent representing a like or dislike action.
         * @return Updated LikeAggregation instance.
         */
        public LikeAggregation update(RoutineLikeEvent event) {
            if (this.ownerId == null) {
                this.ownerId = event.ownerId();
            }
            if (this.routineName == null) {
                this.routineName = event.routineName();
            }
            this.likesCount += event.isLiked() ? 1 : -1;
            return this;
        }
    }
}
