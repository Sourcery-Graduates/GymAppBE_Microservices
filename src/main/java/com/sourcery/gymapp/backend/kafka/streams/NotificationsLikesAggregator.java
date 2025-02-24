package com.sourcery.gymapp.backend.kafka.streams;

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

@Slf4j
@Component
public class NotificationsLikesAggregator {
    @Value("${spring.kafka.topics.likes-events}")
    private String INPUT_TOPIC;
    @Value("${spring.kafka.topics.likes-notifications}")
    private String OUTPUT_TOPIC;
    private final ObjectMapper objectMapper;

    public NotificationsLikesAggregator() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }


    @Bean
    public KStream<String, LikeNotificationEvent> notificationLikesStream(StreamsBuilder builder) {
        JsonSerde<RoutineLikeEvent> routineLikeSerde = new JsonSerde<>(RoutineLikeEvent.class);
        JsonSerde<LikeAggregation> aggregationSerde = new JsonSerde<>(LikeAggregation.class);
        JsonSerde<LikeNotificationEvent> notificationSerde = new JsonSerde<>(LikeNotificationEvent.class);

        KStream<String, RoutineLikeEvent> inputStream = builder
                .stream(INPUT_TOPIC, Consumed.with(Serdes.String(), routineLikeSerde));

        KTable<Windowed<String>, LikeAggregation> aggregatedLikes = inputStream
                .groupByKey()
                .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofDays(3)))
                .aggregate(
                        LikeAggregation::new,
                        (routineId, event, aggregation) -> aggregation.update(event),
                        Materialized.with(Serdes.String(), aggregationSerde)
                );

        KStream<String, LikeNotificationEvent> outputStream = aggregatedLikes
                .toStream((windowedKey, value) -> windowedKey.key())
                .mapValues((routineId, aggregation) -> new LikeNotificationEvent(
                        aggregation.ownerId, UUID.fromString(routineId), aggregation.likesCount
                ));

        outputStream.to(OUTPUT_TOPIC, Produced.with(Serdes.String(), notificationSerde));

        return outputStream;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class LikeAggregation {
        private UUID ownerId;
        private long likesCount = 0;

        public LikeAggregation update(RoutineLikeEvent event) {
            if (this.ownerId == null) {
                this.ownerId = event.ownerId();
            }
            this.likesCount += event.isLiked() ? 1 : -1;
            return this;
        }
    }
}
