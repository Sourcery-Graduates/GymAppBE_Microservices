package com.sourcery.gymapp.backend.email.service;

import com.sourcery.gymapp.backend.events.EmailSendEvent;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import static org.mockito.Mockito.*;

import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@SpringBootTest
public class EmailServiceTest {

    @MockBean
    private KafkaTemplate<String, EmailSendEvent> kafkaTemplate;

    @MockBean
    private JavaMailSender mailSender;

    @MockBean
    private MimeMessage mimeMessage;

    @Value("${spring.kafka.topics.email-retry}")
    private String EMAIL_RETRY_TOPIC;

    @Autowired
    private EmailService emailService;

    @Nested
    @DisplayName("Send Email")
    public class SentEmail{
        @Test
        public void testEmailWasSent() {

            EmailSendEvent emailSendEvent = new EmailSendEvent("testSubject", "TestSenderName", "testContent", "email@email.com");

            when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

            emailService.sendEmail(emailSendEvent);

            verify(mailSender, times(1)).send(any(MimeMessage.class));
        }

        @Test
        public void testSendEmail_ThrowsMailException_retriesEmail() {
            EmailSendEvent emailSendEvent = new EmailSendEvent("testSubject", "TestSenderName", "testContent", "email@email.com");

            when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
            doThrow(new MailException("Error sending email") {}).when(mailSender).send(any(MimeMessage.class));

            emailService.sendEmail(emailSendEvent);

            verify(kafkaTemplate, times(1)).send(EMAIL_RETRY_TOPIC, emailSendEvent);
        }
    }
}
