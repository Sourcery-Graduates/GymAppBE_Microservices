package com.sourcery.gymapp.backend.authentication.event;

import com.sourcery.gymapp.backend.authentication.model.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class PasswordResetEvent extends ApplicationEvent {
    private User user;
    private String applicationURL;


    public PasswordResetEvent(User user, String applicationURL) {
        super(user);
        this.user = user;
        this.applicationURL = applicationURL;
    }
}
