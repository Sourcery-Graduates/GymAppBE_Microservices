package com.sourcery.gymapp.email.event;

public record EmailSendEvent(
        String subject,
        String senderName,
        String content,
        String userEmail,
        int retryCount
) {
}
