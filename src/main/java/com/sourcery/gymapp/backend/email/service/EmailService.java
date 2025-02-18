package com.sourcery.gymapp.backend.email.service;

import com.sourcery.gymapp.backend.events.EmailSendEvent;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String emailServiceAddress;

    public void sendEmail(EmailSendEvent emailDto) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom(emailServiceAddress, emailDto.senderName());
        messageHelper.setTo(emailDto.userEmail());
        messageHelper.setSubject(emailDto.subject());
        messageHelper.setText(emailDto.content(), true);
        mailSender.send(message);
    }

    @ApplicationModuleListener
    public void getEmailEvent(EmailSendEvent eventDto) throws MessagingException, UnsupportedEncodingException {
        sendEmail(eventDto);
    }
}
