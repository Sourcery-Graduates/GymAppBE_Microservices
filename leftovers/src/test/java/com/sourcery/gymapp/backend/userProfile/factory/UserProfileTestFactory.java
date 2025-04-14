package com.sourcery.gymapp.userProfile.factory;

import com.sourcery.gymapp.backend.userProfile.dto.UserProfileDto;
import com.sourcery.gymapp.backend.userProfile.model.UserProfile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class UserProfileTestFactory {

    public static UserProfile createUserProfile(UUID userId,
                                         String userName,
                                         String firstName,
                                         String lastName,
                                         String bio,
                                         String avatarUrl,
                                         String location,
                                         Map<String, Object> settings,
                                         UUID id,
                                         LocalDateTime createdAt,
                                         LocalDateTime modifiedAt) {
    UserProfile userProfile = new UserProfile();

    userProfile.setUserId(userId);
    userProfile.setUsername(userName);
    userProfile.setFirstName(firstName);
    userProfile.setLastName(lastName);
    userProfile.setBio(bio);
    userProfile.setAvatarUrl(avatarUrl);
    userProfile.setLocation(location);
    userProfile.setSettings(settings);
    userProfile.setId(id);
    userProfile.setCreatedBy(userId);
    userProfile.setModifiedBy(userId);
    userProfile.setCreatedAt(createdAt);
    userProfile.setModifiedAt(modifiedAt);

        return userProfile;
    }

    public static UserProfile createUserProfile(UUID userId,
                                         String userName,
                                         String firstName,
                                         String lastName,
                                         String bio,
                                         String avatarUrl,
                                         String location,
                                         Map<String, Object> settings)
    {
       return createUserProfile(userId,
                userName,
                firstName,
                lastName,
                bio,
                avatarUrl,
                location,
                settings,
                UUID.randomUUID(),
                LocalDateTime.now(),
                LocalDateTime.now()
                );
    }
    public static UserProfile createUserProfile(UUID userId,
                                         String userName,
                                         String firstName,
                                         String lastName)
    {
        return createUserProfile(userId,
                userName,
                firstName,
                lastName,
                "TestBio",
                "TestAvatarUrl",
                "TestLocation",
                createSettings());
    }
    public static UserProfile createUserProfile() {

        return createUserProfile(UUID.randomUUID(),
                String.format("testUserName%s", UUID.randomUUID()),
                "testFirstName",
                "testLastName");
    }

    public static Map<String,Object> createSettings(){
        Map<String,Object> settings = new HashMap<>();
        settings.put("test1", "allow");
        settings.put("test2", "disabled");

        return settings;
    }

    public static UserProfileDto createUserProfileDtoFromEntity(UserProfile userProfile){
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

    public static UserProfile createUserProfileFromDto(UserProfileDto dto){
        UUID currentUserId = UUID.randomUUID();
        UUID userProfileId= UUID.randomUUID();

        UserProfile userProfile = new UserProfile();
        userProfile.setUserId(currentUserId);
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
}
