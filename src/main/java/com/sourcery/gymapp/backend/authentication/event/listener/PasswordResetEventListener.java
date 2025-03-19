package com.sourcery.gymapp.backend.authentication.event.listener;

import com.sourcery.gymapp.backend.authentication.event.PasswordResetEvent;
import com.sourcery.gymapp.backend.authentication.model.EmailToken;
import com.sourcery.gymapp.backend.authentication.model.TokenType;
import com.sourcery.gymapp.backend.authentication.repository.EmailTokenRepository;
import com.sourcery.gymapp.backend.events.EmailSendEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.sourcery.gymapp.backend.authentication.model.User;

import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PasswordResetEventListener implements ApplicationListener<PasswordResetEvent> {

    private final EmailTokenRepository emailTokenRepository;
    private final ApplicationEventPublisher emailPublisher;

    @Value("${frontend.password_reset_path}")
    private String endpoint;

    @Override
    public void onApplicationEvent(PasswordResetEvent event) {
        User user = event.getUser();

        List<EmailToken> emailTokens = emailTokenRepository.findAllByUserIdAndType(user.getId(), TokenType.PASSWORD_RECOVERY);

        if (!emailTokens.isEmpty()) {
            emailTokenRepository.deleteAll(emailTokens);
        }

        String verificationToken = UUID.randomUUID().toString();

        EmailToken emailToken = new EmailToken();

        emailToken.setType(TokenType.PASSWORD_RECOVERY);
        emailToken.setExpirationTime(ZonedDateTime.now().plusHours(24));
        emailToken.setToken(verificationToken);
        emailToken.setUser(user);

        emailTokenRepository.save(emailToken);

        String resetPasswordURL = event.getApplicationURL() + endpoint + verificationToken;
        sendPasswordResetEmail(user, resetPasswordURL);
    }

    public void sendPasswordResetEmail(User user, String resetPasswordURL) {
        String subject = "Password Reset";
        String senderName = "Password Reset Portal Service";
        String mailContent = "<p> Hi, %s </p>".formatted(user.getUsername()) +
                "<p>We received a request to reset your password. Click the link below to set a new password:</p>" +
                "<a href=\"" + resetPasswordURL + "\">Reset Password</a>" +
                "<p>If you did not request this, please ignore this email — your password will remain unchanged.</p>" +
                "<p>For security reasons, this link will expire in 24 hours.</p>" +
                "<p>All previous emails and their links about password reset are invalidated.</p>" +
                "<p>Best regards <br> Gym app <br> Password Reset Portal Service</p>" +
                "<p style=\"font-style: italic; text-decoration: underline;\"> This is automated message, please dont reply to it</p>";

        // can utilize kafka here for email sending
        emailPublisher.publishEvent(new EmailSendEvent(subject, senderName, mailContent, user.getEmail(), 0));
    }
}
