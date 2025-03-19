package com.sourcery.gymapp.backend.email.mapper;

import com.sourcery.gymapp.backend.events.EmailSendEvent;
import org.springframework.stereotype.Component;

@Component
public class EmailMapper {
    public EmailSendEvent incrementRetryEvent(EmailSendEvent event) {
        return  new EmailSendEvent(
                event.subject(),
                event.senderName(),
                event.content(),
                event.userEmail(),
                event.retryCount() + 1
        );
    }
}
