package com.sourcery.gymapp.backend.authentication.validation;

import com.sourcery.gymapp.backend.authentication.dto.RegistrationRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, RegistrationRequest> {
    @Override
    public boolean isValid(RegistrationRequest request, ConstraintValidatorContext context) {
        return request.getPassword().equals(request.getConfirmPassword());
    }
}
