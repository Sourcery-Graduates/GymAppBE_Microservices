package com.sourcery.gymapp.authentication.event;

public record EmailSendEvent(
        String subject,
        String senderName,
        String content,
        String userEmail,
        int retryCount
) {
}
