package com.sourcery.gymapp.authentication.config.security.login;

import com.sourcery.gymapp.authentication.exception.UserAccountNotVerifiedException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {

        String errorParam = "error=invalid";

        Throwable cause = exception.getCause();
        if (cause instanceof UserAccountNotVerifiedException) {
            errorParam = "error=not_verified";
        }

        setDefaultFailureUrl("/login?" + errorParam);

        super.onAuthenticationFailure(request, response, exception);
    }
}
