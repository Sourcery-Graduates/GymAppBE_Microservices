package com.sourcery.gymapp.userProfile.service;

import com.sourcery.gymapp.userProfile.event.RegistrationEvent;
import com.sourcery.gymapp.userProfile.dto.UserProfileDto;
import com.sourcery.gymapp.userProfile.exception.UserProfileNotFoundException;
import com.sourcery.gymapp.userProfile.exception.UserProfileRuntimeException;
import com.sourcery.gymapp.userProfile.model.UserProfile;
import com.sourcery.gymapp.userProfile.repository.UserProfileRepository;
import com.sourcery.gymapp.userProfile.mapper.UserProfileMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final ProfileCurrentUserService currentUserService;
    private final UserProfileRepository userProfileRepository;
    private final UserProfileMapper userProfileMapper;

    public UserProfileDto getUserProfile() {

        UserProfile userProfile = getCurrentUserProfile();

        return userProfileMapper.toDto(userProfile);
    }

    @Transactional
    public UserProfileDto updateUserProfile(UserProfileDto dto) {

        UUID currentUserId = currentUserService.getCurrentUserId();
        UUID userProfileId = userProfileRepository.findUserProfileByUserId(currentUserId)
                .map(UserProfile::getId)
                .orElse(null);

        UserProfile entity = userProfileRepository.save(userProfileMapper.toEntity(dto, currentUserId, userProfileId));
        return userProfileMapper.toDto(entity);
    }

    @Transactional
    public UserProfileDto deleteUserProfile() {

        UserProfile userProfile = getCurrentUserProfile();

        userProfileRepository.deleteById(userProfile.getId());

        return userProfileMapper.toDto(userProfile);
    }

    @Transactional
    public void createUserProfileAfterRegistration(RegistrationEvent event) {
        boolean profileExists = userProfileRepository.existsById(event.userId());
        if (profileExists) {
            throw new UserProfileRuntimeException("Profile already exists");
        }

        UserProfileDto dto = userProfileMapper.toDto(event);
        UserProfile entity = userProfileMapper.toEntity(dto, event.userId(), null);
        userProfileRepository.save(entity);
    }

    private UserProfile getCurrentUserProfile() {
        UUID currentUserId = currentUserService.getCurrentUserId();
        return userProfileRepository.findUserProfileByUserId(currentUserId)
                .orElseThrow(() -> new UserProfileNotFoundException(currentUserId));
    }
}
