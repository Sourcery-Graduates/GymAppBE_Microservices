package com.sourcery.gymapp.backend.authentication.service;

import com.sourcery.gymapp.backend.authentication.event.RegistrationCompleteEvent;
import com.sourcery.gymapp.backend.authentication.event.listener.RegistrationCompleteEventListener;
import com.sourcery.gymapp.backend.authentication.model.EmailToken;
import com.sourcery.gymapp.backend.authentication.model.User;
import com.sourcery.gymapp.backend.authentication.repository.EmailTokenRepository;
import com.sourcery.gymapp.backend.sharedModule.EmailSendDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;

import static org.mockito.Mockito.*;

class RegistrationCompleteEventListenerTest {


    @Mock
    EmailTokenRepository emailTokenRepository;

    @Mock
    ApplicationEventPublisher emailPublisher;

    @InjectMocks RegistrationCompleteEventListener registrationCompleteEventListener;

    private final String userName = "testUser";

    private final String userEmail = "test@example.com";

    private final String userPassword = "password123";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegistrationEmailEvent(){
        User user = new User();
        user.setUsername(userName);
        user.setEmail(userEmail);
        user.setPassword(userPassword);

        EmailToken emailToken = new EmailToken();

        String applicationURL =  "testApplicationUrl";

        RegistrationCompleteEvent registrationCompleteEvent = new RegistrationCompleteEvent(user, applicationURL);

        when(emailTokenRepository.save(any(EmailToken.class))).thenReturn(emailToken);
        doNothing().when(emailPublisher).publishEvent(any(EmailSendDto.class));

        registrationCompleteEventListener.onApplicationEvent(registrationCompleteEvent);

        verify(emailTokenRepository, times(1)).save(any());
        verify(emailPublisher, times(1)).publishEvent(any(EmailSendDto.class));
    }
}
