package com.sourcery.gymapp.backend.authentication.service;

import com.sourcery.gymapp.backend.authentication.model.OidcUserAttributes;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OidcUserAttributesExtractor {

    public OidcUserAttributes extractUserAttributes(OidcUser oidcUser, OidcUserRequest userRequest) {
        String email = oidcUser.getEmail();
        if (email == null) {
            throw new OAuth2AuthenticationException("Email not provided by OIDC provider");
        }

        String name = oidcUser.getGivenName();
        String provider = userRequest.getClientRegistration().getRegistrationId();
        String providerId = oidcUser.getSubject();

        return new OidcUserAttributes(email, name, provider, providerId);
    }
}
