package com.sourcery.gymapp.backend.events;

import org.jmolecules.event.annotation.Externalized;

@Externalized
public record EmailSendEvent(
        String subject,
        String senderName,
        String content,
        String userEmail
) {
}
