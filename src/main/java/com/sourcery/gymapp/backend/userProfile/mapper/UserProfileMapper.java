package com.sourcery.gymapp.backend.userProfile.mapper;

import com.sourcery.gymapp.backend.userProfile.dto.UserProfileDto;
import com.sourcery.gymapp.backend.userProfile.model.UserProfile;
import com.sourcery.gymapp.backend.userProfile.repository.UserProfileRepository;
import com.sourcery.gymapp.backend.userProfile.service.CurrentUserService;
import lombok.RequiredArgsConstructor;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserProfileMapper {

    private final CurrentUserService currentUserService;
    private final UserProfileRepository userProfileRepository;

    public UserProfileDto toDto(UserProfile entity) {
        return new UserProfileDto(
                entity.getUsername(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getBio(),
                entity.getAvatarUrl(),
                entity.getLocation(),
                entity.getSettings()
        );
    }

    public UserProfile toEntity(UserProfileDto dto) {
        UUID currentUserId = currentUserService.getCurrentUser().getId();
        UserProfile userProfile = userProfileRepository.findUserProfileByUserId(currentUserId);

        UserProfile entity = new UserProfile();
        entity.setUserId(currentUserId);
        entity.setId(userProfile==null ? null : userProfile.getId());
        entity.setUsername(dto.username());
        entity.setFirstName(dto.firstName());
        entity.setLastName(dto.lastName());
        entity.setBio(dto.bio());
        entity.setAvatarUrl(dto.avatarUrl());
        entity.setLocation(dto.location());
        entity.setSettings(dto.settings());

        return entity;
    }
}
