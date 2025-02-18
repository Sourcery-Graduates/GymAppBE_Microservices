package com.sourcery.gymapp.backend.kafka.streams;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sourcery.gymapp.backend.events.RoutineLikeEvent;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class NotificationsLikesAggregator {
    @Value("${spring.kafka.topics.likes-events}")
    private String INPUT_TOPIC;
    @Value("${spring.kafka.topics.likes-notifications}")
    private String OUTPUT_TOPIC;

    @Bean
    public KStream<UUID, String> notificationLikesStream(StreamsBuilder builder) {
        KStream<UUID, String> stream = builder.stream(INPUT_TOPIC, Consumed.with(
                new Serdes.UUIDSerde(), new Serdes.StringSerde()
        ));
        ObjectMapper objectMapper = new ObjectMapper();

        KTable<UUID, String> reactionCounts = stream.groupBy()

    }
}
