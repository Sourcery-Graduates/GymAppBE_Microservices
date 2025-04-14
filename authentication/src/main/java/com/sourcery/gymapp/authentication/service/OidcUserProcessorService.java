package com.sourcery.gymapp.authentication.service;

import com.sourcery.gymapp.authentication.mapper.UserMapper;
import com.sourcery.gymapp.authentication.model.OidcUserAttributes;
import com.sourcery.gymapp.authentication.model.User;
import com.sourcery.gymapp.authentication.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OidcUserProcessorService {
    private static final String OAUTH2_PASSWORD_PLACEHOLDER = "OAUTH2_ONLY";

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public User processUser(OidcUserAttributes attributes) {
        Optional<User> userOptional = userRepository.findByEmail(attributes.email());
        if (userOptional.isPresent()) {
            updateExistingUser(userOptional.get(), attributes);
            return null;
        } else {
            return createNewUser(attributes);
        }
    }

    private void updateExistingUser(User user, OidcUserAttributes attributes) {
        if (user.getProvider() == null) {
            // TODO: fix this later: currently done because of email confirmation func
            if (!user.isEnabled()) {
                user.setPassword(OAUTH2_PASSWORD_PLACEHOLDER);
                user.setEnabled(true);
            }
            user.setProvider(attributes.provider());
            user.setProviderId(attributes.providerId());
            userRepository.save(user);
            log.info("Linked existing user {} with OIDC provider", attributes.email());
        }
    }

    private User createNewUser(OidcUserAttributes attributes) {
        User newUser = userMapper.createOAuth2User(
                attributes.email(),
                attributes.name(),
                attributes.provider(),
                attributes.providerId()
        );

        newUser.setPassword(OAUTH2_PASSWORD_PLACEHOLDER);
        newUser.setEnabled(true);

        newUser = userRepository.save(newUser);
        log.info("Created new user via OIDC: {}", attributes.email());
        return newUser;
    }
}
