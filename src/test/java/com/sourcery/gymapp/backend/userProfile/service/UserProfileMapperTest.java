package com.sourcery.gymapp.backend.userProfile.service;

import com.sourcery.gymapp.backend.userProfile.factory.UserProfileTestFactory;
import com.sourcery.gymapp.backend.userProfile.mapper.UserProfileMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import com.sourcery.gymapp.backend.userProfile.model.UserProfile;
import com.sourcery.gymapp.backend.userProfile.dto.UserProfileDto;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;

@ExtendWith(MockitoExtension.class)
public class UserProfileMapperTest {
    @InjectMocks
    private UserProfileMapper userProfileMapper;


    @Nested
    @DisplayName("UserProfile to UserProfileDto")
    public class userProfileToUserProfileDto {
        @Test
        void testMapToDto_Success() {
            //given
            UserProfile userProfile = UserProfileTestFactory.createUserProfile();
            UserProfileDto userProfileDto = UserProfileTestFactory.createUserProfileDtoFromEntity(userProfile);

            //when
            UserProfileDto result = userProfileMapper.toDto(userProfile);
            //then
            assertEquals(result, userProfileDto);
        }

        @Test
        void testMapToDto_AppropiateAmountOfFields() {
            int expectedFieldCount = 7;
            //given
            UserProfile userProfile = UserProfileTestFactory.createUserProfile();

            //when
            UserProfileDto result = userProfileMapper.toDto(userProfile);
            Field[] fields = result.getClass().getDeclaredFields();
            //then
            assertEquals(fields.length, expectedFieldCount);
        }
    }

    @Nested
    @DisplayName("UserProfileDto to UserProfile")
    public class userProfileDtoToUserProfile {
        @Test
        void testMapToEntity_Success() {
            //given
            UserProfile userProfile = UserProfileTestFactory.createUserProfile();
            UserProfileDto userProfileDto = UserProfileTestFactory.createUserProfileDtoFromEntity(userProfile);

            //when
            UserProfile result = userProfileMapper.toEntity(userProfileDto, userProfile.getUserId(), userProfile.getId());
            //then
            assertAll(
                    () -> assertEquals(result.getUserId(), userProfile.getUserId()),
                    () -> assertEquals(result.getUsername(), userProfile.getUsername()),
                    () -> assertEquals(result.getFirstName(), userProfile.getFirstName()),
                    () -> assertEquals(result.getLastName(), userProfile.getLastName()),
                    () -> assertEquals(result.getBio(), userProfile.getBio()),
                    () -> assertEquals(result.getAvatarUrl(), userProfile.getAvatarUrl()),
                    () -> assertEquals(result.getLocation(), userProfile.getLocation()),
                    () -> assertEquals(result.getSettings(), userProfile.getSettings())
            );

        }

        @Test
        void testMapToEntity_AppropiateAmountOfFields() {
            int expectedFieldCount = 8;
            //given
            UserProfile userProfile = UserProfileTestFactory.createUserProfile();
            UserProfileDto userProfileDto = UserProfileTestFactory.createUserProfileDtoFromEntity(userProfile);

            //when
            UserProfile result = userProfileMapper.toEntity(userProfileDto, userProfile.getUserId(), userProfile.getId());
            Field[] fields = result.getClass().getDeclaredFields();
            //then
            assertEquals(fields.length, expectedFieldCount);
        }
    }
}
