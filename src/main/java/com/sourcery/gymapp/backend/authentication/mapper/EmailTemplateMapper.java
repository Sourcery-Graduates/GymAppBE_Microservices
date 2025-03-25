package com.sourcery.gymapp.backend.authentication.mapper;

import com.sourcery.gymapp.backend.authentication.model.User;
import com.sourcery.gymapp.backend.events.EmailSendEvent;
import org.springframework.stereotype.Component;

@Component
public class EmailTemplateMapper {

    public EmailSendEvent toRegisterVerificationEmail(User user, String verificationUrl) {
        String subject = "Email verificaton";
        String senderName = "User Registration Portal Service";
        String mailContent = "<p> Hi, %s </p>".formatted(user.getUsername()) +
                "<p>Thank you for registering with us.</p>" +
                "<p>Please, follow the link below to complete your registration. </p>" +
                "<a href=\"" + verificationUrl + "\">Verify your email to activate your account </a>" +
                "<p> Thank you <br> Users Registration Portal Service</p>" +
                "<p style=\"font-style: italic; text-decoration: underline;\"> This is automated message, please dont reply to it</p>";

        return new EmailSendEvent(subject, senderName, mailContent, user.getEmail(), 0);
    }

    public EmailSendEvent toPasswordResetEmail(User user, String resetPasswordUrl) {
        String subject = "Password Reset";
        String senderName = "Password Reset Portal Service";
        String mailContent = "<p> Hi, %s </p>".formatted(user.getUsername()) +
                "<p>We received a request to reset your password. Click the link below to set a new password:</p>" +
                "<a href=\"" + resetPasswordUrl + "\">Reset Password</a>" +
                "<p>If you did not request this, please ignore this email — your password will remain unchanged.</p>" +
                "<p>For security reasons, this link will expire in 24 hours.</p>" +
                "<p>All previous emails and their links about password reset are invalidated.</p>" +
                "<p>Best regards <br> Gym app <br> Password Reset Portal Service</p>" +
                "<p style=\"font-style: italic; text-decoration: underline;\"> This is automated message, please dont reply to it</p>";

        return new EmailSendEvent(subject, senderName, mailContent, user.getEmail(), 0);
    }
}
