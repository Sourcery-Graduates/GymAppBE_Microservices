package com.sourcery.gymapp.backend.authentication.controller;

import com.sourcery.gymapp.backend.authentication.dto.PasswordChangeDto;
import com.sourcery.gymapp.backend.authentication.dto.PasswordResetRequestDto;
import com.sourcery.gymapp.backend.authentication.dto.RegistrationRequest;
import com.sourcery.gymapp.backend.authentication.dto.UserAuthDto;
import com.sourcery.gymapp.backend.authentication.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @GetMapping("/authenticate")
    public UserAuthDto authenticate(Authentication authentication) {
        return authService.authenticateUser(authentication);
    }

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
