package com.sourcery.gymapp.backend.authentication.config;

import com.sourcery.gymapp.backend.authentication.validation.PasswordMatch;
import com.sourcery.gymapp.backend.authentication.validation.PasswordMatchValidator;
import com.sourcery.gymapp.backend.globalconfig.CurrentUserService;
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
