package com.sourcery.gymapp.backend.email.service;

import com.sourcery.gymapp.backend.events.EmailSendEvent;
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
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String emailServiceAddress;

    public boolean sendEmail(EmailSendEvent emailDto) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message);
            messageHelper.setFrom(emailServiceAddress, emailDto.senderName());
            messageHelper.setTo(emailDto.userEmail());
            messageHelper.setSubject(emailDto.subject());
            messageHelper.setText(emailDto.content(), true);
            mailSender.send(message);
            logger.info("Email sent successfully to {}", emailDto.userEmail());
            return true;
        }
        catch (UnsupportedEncodingException e) {
            logger.error("Unsupported encoding in email sender name: {}", emailDto.senderName(), e);
        }
        catch (MessagingException | MailException e) {
            logger.error("Failed to send email to {}", emailDto.userEmail(), e);
        }
        return false;
    }

    @ApplicationModuleListener
    public void getEmailEvent(EmailSendEvent eventDto) {
        sendEmail(eventDto);
    }
}
