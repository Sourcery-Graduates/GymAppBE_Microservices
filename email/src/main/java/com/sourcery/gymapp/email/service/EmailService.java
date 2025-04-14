package com.sourcery.gymapp.email.service;

import com.sourcery.gymapp.email.mapper.EmailMapper;
import com.sourcery.gymapp.email.producer.EmailKafkaProducer;
import com.sourcery.gymapp.email.event.EmailSendEvent;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    private final JavaMailSender mailSender;
    private final EmailKafkaProducer emailKafkaProducer;
    private final EmailMapper emailMapper;
    private final ThreadPoolTaskScheduler taskScheduler;
    private static final int MAX_RETRIES = 3;
    private static final long BASE_BACKOFF_TIME_SECONDS = 2;

    @Value("${spring.mail.username}")
    private String emailServiceAddress;

    public void sendEmail(EmailSendEvent emailDto) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message);
            messageHelper.setFrom(emailServiceAddress, emailDto.senderName());
            messageHelper.setTo(emailDto.userEmail());
            messageHelper.setSubject(emailDto.subject());
            messageHelper.setText(emailDto.content(), true);
            mailSender.send(message);
            logger.info("Email sent successfully to {}", emailDto.userEmail());
            return;
        }
        catch (UnsupportedEncodingException e) {
            logger.error("Unsupported encoding in email sender name: {}", emailDto.senderName(), e);
        }
        catch (MessagingException | MailException e) {
            logger.error("Failed to send email to {}", emailDto.userEmail(), e);
        }
        handleRetry(UUID.randomUUID(), emailDto);
    }

    public void handleRetry(UUID eventId, EmailSendEvent email) {
        if (email.retryCount() < MAX_RETRIES) {
            logger.warn("Retrying email for {} (attempt {}/{})", email.userEmail(), email.retryCount() + 1, MAX_RETRIES);
            EmailSendEvent retriedEmail = emailMapper.incrementRetryEvent(email);
            taskScheduler.schedule(
                    () -> emailKafkaProducer.retryEmail(eventId, retriedEmail),
                    calculateBackoffTime(retriedEmail.retryCount())
            );
        } else {
            logger.error("Error processing email event, Max retries reached: {}", email);
        }
    }

    private Instant calculateBackoffTime(int retryCount) {
        return Instant.now().plusSeconds(BASE_BACKOFF_TIME_SECONDS * retryCount);
    }

    @ApplicationModuleListener
    public void getEmailEvent(EmailSendEvent eventDto) {
        sendEmail(eventDto);
    }
}
