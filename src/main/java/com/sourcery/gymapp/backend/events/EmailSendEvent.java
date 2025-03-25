package com.sourcery.gymapp.backend.events;

public record EmailSendEvent(
        String subject,
        String senderName,
        String content,
        String userEmail,
        int retryCount
) {
}
