package com.sourcery.gymapp.backend.authentication.event.listener;

import com.sourcery.gymapp.backend.authentication.event.RegistrationCompleteEvent;
import com.sourcery.gymapp.backend.authentication.model.EmailToken;
import com.sourcery.gymapp.backend.authentication.model.TokenType;
import com.sourcery.gymapp.backend.authentication.repository.EmailTokenRepository;
import com.sourcery.gymapp.backend.sharedModule.EmailSendDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.sourcery.gymapp.backend.authentication.model.User;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {

    private final EmailTokenRepository emailTokenRepository;
    private final ApplicationEventPublisher emailPublisher;

    @Value("${frontend.registration_verification_path}")
    private String endpoint;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        User user = event.getUser();

        String verificationToken = UUID.randomUUID().toString();

        EmailToken emailToken = new EmailToken();

        emailToken.setType(TokenType.REGISTRATION);
        emailToken.setToken(verificationToken);
        emailToken.setUser(user);

        emailTokenRepository.save(emailToken);

        String verificationURL = event.getApplicationURL() + endpoint + verificationToken;
        sendRegisterVerificationEmail(user, verificationURL);
    }

    public void sendRegisterVerificationEmail(User user, String verificationUrl) {
        String subject = "Email verificaton";
        String senderName = "User Registration Portal Service";
        String mailContent = "<p> Hi, %s </p>".formatted(user.getUsername()) +
                "<p>Thank you for registering with us.</p>" +
                "<p>Please, follow the link below to complete your registration. </p>" +
                "<a href=\"" + verificationUrl + "\">Verify your email to activate your account </a>" +
                "<p> Thank you <br> Users Registration Portal Service</p>" +
                "<p style=\"font-style: italic; text-decoration: underline;\"> This is automated message, please dont reply to it</p>";


        // can utilize kafka here for email sending
        emailPublisher.publishEvent(new EmailSendDto(subject, senderName, mailContent, user.getEmail()));
    }
}
