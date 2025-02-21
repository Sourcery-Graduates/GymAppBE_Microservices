package com.sourcery.gymapp.backend.kafka.streams;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sourcery.gymapp.backend.events.RoutineLikeEvent;
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
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.UUID;

@Component
public class NotificationsLikesAggregator {
    @Value("${spring.kafka.topics.likes-events}")
    private String INPUT_TOPIC;
    @Value("${spring.kafka.topics.likes-notifications}")
    private String OUTPUT_TOPIC;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Bean
    public KTable<String, Long> notificationLikesStream(StreamsBuilder builder) {

        KStream<String, String> textLines = builder.stream(
                INPUT_TOPIC, Consumed.with(Serdes.String(), Serdes.String())
        );

        KTable<String, Long> outputStream = textLines
                .groupByKey()
                .count();

        outputStream.toStream().to(OUTPUT_TOPIC, Produced.with(Serdes.String(), Serdes.Long()));
        return outputStream;

//        KStream<UUID, RoutineLikeEvent> inputStream = builder
//                .stream(INPUT_TOPIC,
//                        Consumed.with(new Serdes.UUIDSerde(), new Serdes.StringSerde())
//                ).mapValues((value) -> {
//                    try {
//                        return objectMapper.readValue(value, RoutineLikeEvent.class);
//                    } catch (Exception e) {
//                        return null;
//                    }
//                }).filter((key, value) -> value != null);
//
//
//        KTable<Windowed<UUID>, Long> reactionCounts = inputStream
//                .groupByKey(Grouped.with(new Serdes.UUIDSerde(), Serdes.serdeFrom(RoutineLikeEvent.class)))
//                .windowedBy(TimeWindows.of(Duration.ofDays(7)))
//                .count(Materialized.as("likes-aggregated-store"));
//
//        KStream<UUID, String> outputStream = reactionCounts
//                .toStream((windowedKey, value) -> windowedKey.key())
//                .mapValues(value -> {
//                    try {
//                        return objectMapper.writeValueAsString(new LikeNotification(value));
//                    } catch (Exception e) {
//                        return "{}";
//                    }
//                });
//
//        outputStream.to(OUTPUT_TOPIC, Produced.with(new Serdes.UUIDSerde(), Serdes.String()));

//        return outputStream;
    }
}
