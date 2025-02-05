package com.sourcery.gymapp.backend.globalconfig;

import lombok.Getter;
import lombok.Setter;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "spring.kafka")
public class KafkaTopicCreationConfig {

    private Map<String, String> topics;
    private Integer partitions;
    private Integer replicas;


    @Bean
    public KafkaAdmin.NewTopics createTopics() {
        List<NewTopic> listOfTopics = new ArrayList<>();

        for (Map.Entry<String, String> entry : topics.entrySet()) {
            listOfTopics.add(
                    TopicBuilder.name(entry.getValue())
                            .partitions(replicas)
                            .replicas(partitions)
                            .build()
            );
        }

        return new KafkaAdmin.NewTopics(listOfTopics.toArray(new NewTopic[0]));
    }
}


