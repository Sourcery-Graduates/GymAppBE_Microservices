package com.sourcery.gymapp.backend.authentication.service;

import com.sourcery.gymapp.backend.authentication.dto.RegistrationRequest;
import com.sourcery.gymapp.backend.authentication.dto.UserAuthDto;
import com.sourcery.gymapp.backend.authentication.dto.UserDetailsDto;
import com.sourcery.gymapp.backend.authentication.jwt.GymAppJwtProvider;
import com.sourcery.gymapp.backend.authentication.mapper.UserMapper;
import com.sourcery.gymapp.backend.authentication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//TODO: add proper exception handling
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final GymAppJwtProvider jwtProvider;

    @Transactional(readOnly = true)
    public UserAuthDto authenticateUser(Authentication authentication) {
        if (authentication.getPrincipal() instanceof UserDetails userDetails) {
            String token = jwtProvider.generateToken(userDetails.getUsername());
            return userMapper.toAuthDto((UserDetailsDto) userDetails, token);
        }
        if (authentication.getPrincipal() instanceof Jwt jwt) {
            UserDetailsDto userDetails =
                    (UserDetailsDto) userDetailsService.loadUserByUsername(jwt.getClaim("username"));
            String token = jwt.getTokenValue();
            return userMapper.toAuthDto(userDetails, token);
        }
        throw new RuntimeException("USER NOT AUTHENTICATED EXCEPTION");
    }

    @Transactional
    public void register(RegistrationRequest registrationRequest) {
        if (userRepository.existsByUsername(registrationRequest.getUsername())) {
            throw new IllegalStateException("Username is already taken");
        }

        registrationRequest.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        userRepository.save(userMapper.toEntity(registrationRequest));
    }
}
