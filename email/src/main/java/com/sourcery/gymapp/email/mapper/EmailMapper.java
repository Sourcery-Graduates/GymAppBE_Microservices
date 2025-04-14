package com.sourcery.gymapp.email.mapper;

import com.sourcery.gymapp.email.event.EmailSendEvent;
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
