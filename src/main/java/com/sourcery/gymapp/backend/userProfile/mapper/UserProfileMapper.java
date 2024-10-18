package com.sourcery.gymapp.backend.userProfile.mapper;

import com.sourcery.gymapp.backend.globalConfig.AuditorConfig;
import com.sourcery.gymapp.backend.userProfile.dto.UserProfileDto;
import com.sourcery.gymapp.backend.userProfile.exception.UserNotFoundException;
import com.sourcery.gymapp.backend.userProfile.model.UserProfile;
import com.sourcery.gymapp.backend.userProfile.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import java.util.UUID;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserProfileMapper {

    private final AuditorAware<UUID> auditorAware;
    private final UserProfileRepository userProfileRepository;

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

    public UserProfile toEntity(UserProfileDto dto) {
        UUID currentUserId = auditorAware.getCurrentAuditor().orElseThrow(UserNotFoundException::new);
        UserProfile userProfileInDB = userProfileRepository.findUserProfileByUserId(currentUserId);

        UserProfile userProfile = new UserProfile();
        userProfile.setUserId(currentUserId);
        userProfile.setId(userProfileInDB==null ? null : userProfileInDB.getId());
        userProfile.setUsername(dto.username());
        userProfile.setFirstName(dto.firstName());
        userProfile.setLastName(dto.lastName());
        userProfile.setBio(dto.bio());
        userProfile.setAvatarUrl(dto.avatarUrl());
        userProfile.setLocation(dto.location());
        userProfile.setSettings(dto.settings());

        return userProfile;
    }
}
