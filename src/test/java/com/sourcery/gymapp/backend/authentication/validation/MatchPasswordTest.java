package com.sourcery.gymapp.backend.authentication.validation;

import com.sourcery.gymapp.backend.authentication.dto.RegistrationRequest;
import com.sourcery.gymapp.backend.authentication.factory.RegistrationRequestFactory;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

public class MatchPasswordTest {
    private PasswordMatchValidator validator;
    @Mock
    private ConstraintValidatorContext context;

    @BeforeEach
    public void setUp() {
        validator = new PasswordMatchValidator();
    }

    @Test
    public void testRequestWithTheMatchingPasswords_ShouldPassValidation() {
        RegistrationRequest request = RegistrationRequestFactory.createRegistrationValidRequest();

        boolean result = validator.isValid(request, context);

        assert(result);
    }

    @Test
    public void testRequestWithDifferentPasswords_ShouldFailValidation() {
        RegistrationRequest request = RegistrationRequestFactory.createRegistrationRequestDifferentPasswords();

        boolean result = validator.isValid(request, context);

        assert(!result);
    }


}
