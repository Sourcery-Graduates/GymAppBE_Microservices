package com.sourcery.gymapp.backend.authentication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sourcery.gymapp.backend.authentication.dto.RegistrationRequest;
import com.sourcery.gymapp.backend.authentication.factory.RegistrationRequestFactory;
import com.sourcery.gymapp.backend.authentication.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;


    @Test
    void registrationWithDifferentPasswords_ShouldNotTriggerService() throws Exception {
        RegistrationRequest request = RegistrationRequestFactory.createRegistrationRequestDifferentPasswords();

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        Mockito.verify(authService, Mockito.never()).register(Mockito.any());
    }

    @Test
    void registrationWithSamePasswords_ShouldTriggerService() throws Exception {
        RegistrationRequest request = RegistrationRequestFactory.createRegistrationValidRequest();

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        Mockito.verify(authService, Mockito.times(1)).register(Mockito.any());
    }
}
