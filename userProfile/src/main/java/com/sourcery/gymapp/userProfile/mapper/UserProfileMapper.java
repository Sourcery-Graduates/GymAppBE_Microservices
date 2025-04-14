package com.sourcery.gymapp.userProfile.mapper;

import com.sourcery.gymapp.userProfile.event.RegistrationEvent;
import com.sourcery.gymapp.userProfile.dto.UserProfileDto;
import com.sourcery.gymapp.userProfile.model.UserProfile;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserProfileMapper {

    public UserProfileDto toDto(UserProfile userProfile) {
        return new UserProfileDto(
                userProfile.getUsername(),
                userProfile.getFirstName(),
                userProfile.getLastName(),
                userProfile.getBio(),
                userProfile.getAvatarUrl(),
                userProfile.getLocation(),
                userProfile.getSettings()
        );
    }

    public UserProfile toEntity(UserProfileDto dto, UUID userId, UUID userProfileId) {
        UserProfile userProfile = new UserProfile();
        userProfile.setUserId(userId);
        userProfile.setId(userProfileId);
        userProfile.setUsername(dto.username());
        userProfile.setFirstName(dto.firstName());
        userProfile.setLastName(dto.lastName());
        userProfile.setBio(dto.bio());
        userProfile.setAvatarUrl(dto.avatarUrl());
        userProfile.setLocation(dto.location());
        userProfile.setSettings(dto.settings());

        return userProfile;
    }

    public UserProfileDto toDto(RegistrationEvent event) {
        return new UserProfileDto(
                event.username(),
                event.firstName(),
                event.lastName(),
                event.bio(),
                null,
                event.location(),
                new HashMap<>()
        );
    }
}
