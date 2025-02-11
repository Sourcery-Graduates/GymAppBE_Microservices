package com.sourcery.gymapp.backend.authentication.service;

import com.sourcery.gymapp.backend.authentication.dto.RegistrationRequest;
import com.sourcery.gymapp.backend.authentication.exception.UserAlreadyExistsException;
import com.sourcery.gymapp.backend.authentication.mapper.UserMapper;
import com.sourcery.gymapp.backend.authentication.model.User;
import com.sourcery.gymapp.backend.authentication.producer.AuthKafkaProducer;
import com.sourcery.gymapp.backend.authentication.repository.UserRepository;
import com.sourcery.gymapp.backend.events.RegistrationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthKafkaProducer kafkaEventsProducer;
    private final TransactionTemplate transactionTemplate;

    public void register(RegistrationRequest registrationRequest) {
        User user = transactionTemplate.execute(status -> {
            if (userRepository.existsByUsername(registrationRequest.getUsername())) {
                throw new UserAlreadyExistsException();
            }

            registrationRequest.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            return userRepository.save(userMapper.toEntity(registrationRequest));
        });

        if (user == null) {
            throw new UserAlreadyExistsException();
        }
        RegistrationEvent event = userMapper.toRegistrationEvent(user, registrationRequest);
        kafkaEventsProducer.sendRegistrationEvent(event);
    }
}
