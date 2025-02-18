package com.sourcery.gymapp.backend.config.integration;

import com.sourcery.gymapp.backend.email.service.EmailService;
import com.sourcery.gymapp.backend.events.EmailSendEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;


@Configuration
@Profile("test")
public class TestEmailService {

    @Bean
    @Primary
    public EmailService emailService(JavaMailSender javaMailSender) {
        return new EmailService(javaMailSender) {
            @Override
            public void sendEmail(EmailSendEvent emailDto) {
                System.out.printf("Registration event email processed: subject: %s, senderName: %s, content: %s, userEmail: %s".formatted(emailDto.subject(), emailDto.senderName(), emailDto.content(), emailDto.userEmail()));
            }

            @Override
            public void getEmailEvent(EmailSendEvent eventDto) {
                System.out.println("Received email event: subject: %s, senderName: %s, content: %s, userEmail: %s".formatted(eventDto.subject(), eventDto.senderName(), eventDto.content(), eventDto.userEmail()));
            }
        };
    }
}

