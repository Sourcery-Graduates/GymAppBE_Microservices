package com.sourcery.gymapp.backend.authentication.service;

import com.sourcery.gymapp.backend.authentication.model.User;
import com.sourcery.gymapp.backend.authentication.producer.AuthKafkaProducer;
import com.sourcery.gymapp.backend.events.RegistrationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OidcUserEventService {
    private static final String DEFAULT_LOCATION = "Planet Earth";
    private static final String DEFAULT_BIO = "Gym App Enthusiast";

    private final AuthKafkaProducer kafkaEventsProducer;

    public void sendUserCreationEvents(User user, OidcUser oidcUser) {
        RegistrationEvent event = createRegistrationEvent(user, oidcUser);
        kafkaEventsProducer.sendRegistrationEvent(event);
        log.info("Sent registration event for OIDC user: {}", user.getEmail());
    }

    private RegistrationEvent createRegistrationEvent(User user, OidcUser oidcUser) {
        return new RegistrationEvent(
                user.getId(),
                user.getUsername(),
                oidcUser.getGivenName(),
                oidcUser.getFamilyName(),
                DEFAULT_LOCATION,
                DEFAULT_BIO
        );
    }
}
