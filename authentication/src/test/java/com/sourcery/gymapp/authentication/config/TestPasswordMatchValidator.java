package com.sourcery.gymapp.authentication.config;

import com.sourcery.gymapp.authentication.validation.PasswordMatch;
import com.sourcery.gymapp.authentication.validation.PasswordMatchValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

public class TestPasswordMatchValidator {

    @Bean
    @Primary
    public PasswordMatchValidator passwordMatchValidator() {

        return new PasswordMatchValidator() {
            private String passwordField = "password";
            private String confirmPasswordField = "confirmPassword";

            @Override
            public void initialize(PasswordMatch constraintAnnotation) {
            }
        };
    }
}
