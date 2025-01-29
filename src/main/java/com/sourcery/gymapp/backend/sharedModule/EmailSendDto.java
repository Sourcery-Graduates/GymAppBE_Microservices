package com.sourcery.gymapp.backend.sharedModule;

import org.jmolecules.event.annotation.Externalized;

@Externalized
public record EmailSendDto(
        String subject,
        String senderName,
        String content,
        String userEmail
) {
}
