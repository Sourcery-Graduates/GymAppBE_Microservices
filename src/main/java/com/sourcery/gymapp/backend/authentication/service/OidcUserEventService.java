package com.sourcery.gymapp.backend.authentication.service;

import com.sourcery.gymapp.backend.authentication.config.OidcDefaults;
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
    private final AuthKafkaProducer kafkaEventsProducer;
    private final OidcDefaults oidcDefaults;

    public void sendUserCreationEvents(User user, OidcUser oidcUser) {
        RegistrationEvent event = createRegistrationEvent(user, oidcUser);
        kafkaEventsProducer.sendRegistrationEvent(event);
        log.info("Sent registration event for OIDC user: {}", user.getEmail());
    }

    private RegistrationEvent createRegistrationEvent(User user, OidcUser oidcUser) {
        String givenName = oidcUser.getGivenName();
        String familyName = oidcUser.getFamilyName();

        if (givenName == null || givenName.trim().isEmpty()) {
            givenName = oidcDefaults.getDefaultGivenName();
        }

        if (familyName == null || familyName.trim().isEmpty()) {
            familyName = oidcDefaults.getDefaultFamilyName();
        }

        return new RegistrationEvent(
                user.getId(),
                user.getUsername(),
                givenName,
                familyName,
                oidcDefaults.getDefaultLocation(),
                oidcDefaults.getDefaultBio()
        );
    }
}
