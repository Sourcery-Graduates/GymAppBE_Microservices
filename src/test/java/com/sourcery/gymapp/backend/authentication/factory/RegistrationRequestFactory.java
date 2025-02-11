package com.sourcery.gymapp.backend.authentication.factory;

import com.sourcery.gymapp.backend.authentication.dto.RegistrationRequest;

public class RegistrationRequestFactory {
    public static RegistrationRequest createRegistrationValidRequest() {
        RegistrationRequest request = new RegistrationRequest();
        request.setUsername("testUser");
        request.setPassword("securePassword");
        request.setConfirmPassword("securePassword");
        request.setEmail("test@example.com");
        request.setFirstName("Test");
        request.setLastName("User");
        request.setBio("Lorem ipsum dolor sit amet");

        return request;
    }

    public static RegistrationRequest createRegistrationRequestDifferentPasswords() {
        RegistrationRequest request = createRegistrationValidRequest();
        request.setConfirmPassword("securePassworq");
        return request;
    }
}
