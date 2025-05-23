package com.sourcery.gymapp.authentication.controller;

import com.sourcery.gymapp.authentication.dto.PasswordChangeDto;
import com.sourcery.gymapp.authentication.dto.PasswordResetRequestDto;
import com.sourcery.gymapp.authentication.dto.RegistrationRequest;
import com.sourcery.gymapp.authentication.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegistrationRequest registrationRequest) {
        authService.register(registrationRequest);
        return ResponseEntity.ok("User registered successfully");
    }

    @GetMapping("/register/verification")
    public ResponseEntity<String> registerEmailVerification(@RequestParam("token") String token) {
        return authService.registerVerification(token);
    }

    @PostMapping("/password/reset")
        public ResponseEntity<String> passwordResetRequest(@Valid @RequestBody PasswordResetRequestDto passwordResetRequestDto) {
        return authService.passwordResetRequest(passwordResetRequestDto.email());
    }

    @PostMapping("/password/change")
    public ResponseEntity<String> passwordChange(@Valid @RequestBody PasswordChangeDto passwordChangeDto) {
        return authService.passwordChange(passwordChangeDto.password(), passwordChangeDto.token());
    }

}
