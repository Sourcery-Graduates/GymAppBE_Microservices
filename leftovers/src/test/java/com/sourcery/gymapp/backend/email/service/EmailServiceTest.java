package com.sourcery.gymapp.email.service;

import com.sourcery.gymapp.backend.email.mapper.EmailMapper;
import com.sourcery.gymapp.backend.email.producer.EmailKafkaProducer;
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
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@SpringBootTest
public class EmailServiceTest {

    @MockBean
    EmailKafkaProducer emailKafkaProducer;

    @MockBean
    private JavaMailSender mailSender;

    @Autowired
    private EmailMapper emailMapper;

    @MockBean
    private MimeMessage mimeMessage;

    @Value("${spring.kafka.topics.email-retry}")
    private String EMAIL_RETRY_TOPIC;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ThreadPoolTaskScheduler taskScheduler;

    @Nested
    @DisplayName("Send Email")
    public class SentEmail{
        @Test
        public void testEmailWasSent() {

            EmailSendEvent emailSendEvent = new EmailSendEvent("testSubject", "TestSenderName", "testContent", "email@email.com", 0);

            when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

            emailService.sendEmail(emailSendEvent);

            verify(mailSender, times(1)).send(any(MimeMessage.class));
            verify(emailKafkaProducer, never()).retryEmail(any(), any());
        }

        @Test
        public void testSendEmail_ThrowsMailException_retriesEmail() {
            EmailSendEvent emailSendEvent = new EmailSendEvent("testSubject", "TestSenderName", "testContent", "email@email.com", 0);

            when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
            doThrow(new MailException("Error sending email") {}).when(mailSender).send(any(MimeMessage.class));

            emailService.sendEmail(emailSendEvent);
            taskScheduler.getScheduledThreadPoolExecutor().getQueue().forEach(Runnable::run);
            verify(emailKafkaProducer, atLeastOnce()).retryEmail(any(), any());
        }
    }
}
