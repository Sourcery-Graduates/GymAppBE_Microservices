package com.sourcery.gymapp.email.service;

import com.sourcery.gymapp.email.mapper.EmailMapper;
import com.sourcery.gymapp.email.producer.EmailKafkaProducer;
import com.sourcery.gymapp.email.event.EmailSendEvent;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.lang.reflect.Field;
import java.time.Instant;

import static org.mockito.Mockito.*;

import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @Mock
    EmailKafkaProducer emailKafkaProducer;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private EmailMapper emailMapper;

    @Mock
    private MimeMessage mimeMessage;

    @InjectMocks
    private EmailService emailService;

    @Mock
    private ThreadPoolTaskScheduler taskScheduler;

    @BeforeEach
    void injectMailUsername() throws Exception {
        Field field = EmailService.class.getDeclaredField("emailServiceAddress");
        field.setAccessible(true);
        field.set(emailService, "testSender@example.com");
    }


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

            when(emailMapper.incrementRetryEvent(any())).thenAnswer(inv -> {
                EmailSendEvent input = inv.getArgument(0);
                return copyWithRetry(input, input.retryCount() + 1);
            });
            when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
            doThrow(new MailException("Error sending email") {}).when(mailSender).send(any(MimeMessage.class));

            emailService.sendEmail(emailSendEvent);

            ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);
            verify(taskScheduler).schedule(runnableCaptor.capture(), any(Instant.class));
            runnableCaptor.getValue().run();

            verify(emailKafkaProducer, atLeastOnce()).retryEmail(any(), any());
        }
    }

    private static EmailSendEvent copyWithRetry(EmailSendEvent e, int retryCount) {
        return new EmailSendEvent(e.subject(), e.senderName(), e.content(), e.userEmail(), retryCount);
    }
}
