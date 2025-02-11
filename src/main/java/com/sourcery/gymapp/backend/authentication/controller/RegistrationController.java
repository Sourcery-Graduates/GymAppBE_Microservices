package com.sourcery.gymapp.backend.authentication.controller;

import com.sourcery.gymapp.backend.authentication.dto.RegistrationRequest;
import com.sourcery.gymapp.backend.authentication.service.RegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class RegistrationController {
    private final RegistrationService registrationService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegistrationRequest registrationRequest) {
        registrationService.register(registrationRequest);
        return ResponseEntity.ok("User registered successfully");
    }
}
