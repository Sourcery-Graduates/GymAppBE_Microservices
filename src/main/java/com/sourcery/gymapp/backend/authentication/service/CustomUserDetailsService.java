package com.sourcery.gymapp.backend.authentication.service;

import com.sourcery.gymapp.backend.authentication.exception.UserAccountNotVerifiedException;
import com.sourcery.gymapp.backend.authentication.mapper.UserMapper;
import com.sourcery.gymapp.backend.authentication.model.User;
import com.sourcery.gymapp.backend.authentication.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private UserRepository userRepository;
    private UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException("Can't find user by email " + email));

        if (!user.isEnabled()) {
            throw new UserAccountNotVerifiedException();
        }

        return userMapper.toDetailsDto(user);
    }
}
