package com.sourcery.gymapp.userProfile.service;

import com.sourcery.gymapp.userProfile.dto.UserProfileDto;
import com.sourcery.gymapp.userProfile.exception.UserNotAuthenticatedException;
import com.sourcery.gymapp.userProfile.exception.UserProfileNotFoundException;
import com.sourcery.gymapp.userProfile.factory.UserProfileTestFactory;
import com.sourcery.gymapp.userProfile.mapper.UserProfileMapper;
import com.sourcery.gymapp.userProfile.model.UserProfile;
import com.sourcery.gymapp.userProfile.repository.UserProfileRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;



@ExtendWith(MockitoExtension.class)
class UserProfileServiceTest {

    @Mock
    private ProfileCurrentUserService currentUserService;
    @Mock
    private UserProfileRepository userProfileRepository;

    @Mock
    private UserProfileMapper userProfileMapper;

    @InjectMocks
    private UserProfileService userProfileService;


    @Nested
    @DisplayName("Get user profile tests")
    public class userProfileServiceGetUserProfile {
        @Test
        void testGetUserProfile_Success() throws UserNotAuthenticatedException, UserProfileNotFoundException {
            //given
            UserProfile userProfile = UserProfileTestFactory.createUserProfile();
            UserProfileDto userProfileDto = UserProfileTestFactory.createUserProfileDtoFromEntity(userProfile);

            when(userProfileMapper.toDto(userProfile)).thenReturn(userProfileDto);
            when(currentUserService.getCurrentUserId())
                    .thenReturn(userProfile.getUserId());
            when(userProfileRepository.findUserProfileByUserId(userProfile.getUserId()))
                    .thenReturn(Optional.of(userProfile));

            //when
            UserProfileDto result = userProfileService.getUserProfile();

            //then
            assertAll(
                    () -> assertNotNull(result),
                    () -> assertEquals(userProfileDto, result),
                    () -> verify(userProfileRepository)
                            .findUserProfileByUserId(userProfile.getUserId())
            );

        }

        @Test
        void testGetUserProfile_UserProfileDoesntExist_ThrowsUserNotFoundException() {
            //given
            UserProfile userProfile = UserProfileTestFactory.createUserProfile();

            when(currentUserService.getCurrentUserId()).thenReturn(userProfile.getUserId());
            when(userProfileRepository.findUserProfileByUserId(userProfile.getUserId())).thenReturn(Optional.empty());
            //then
            assertThrows(UserProfileNotFoundException.class, () -> userProfileService.getUserProfile());
        }

 }

    @Nested
    @DisplayName("Update user profile")
    public class userProfileServiceUpdate {
        @Test
        void testUpdateUserProfile_existingUserProfile_Success() {
            //given
            UserProfile userProfile = UserProfileTestFactory.createUserProfile();
            UserProfileDto userProfileDto = UserProfileTestFactory.createUserProfileDtoFromEntity(userProfile);

            when(currentUserService.getCurrentUserId()).thenReturn(userProfile.getUserId());
            when(userProfileRepository.findUserProfileByUserId(userProfile.getUserId())).thenReturn(Optional.of(userProfile));
            when(userProfileMapper.toDto(userProfile)).thenReturn(userProfileDto);
            when(userProfileRepository.save(userProfile)).thenReturn(userProfile);
            when(userProfileMapper.toEntity(userProfileDto, userProfile.getUserId(), userProfile.getId())).thenReturn(userProfile);
            //when
            UserProfileDto result = userProfileService.updateUserProfile(userProfileDto);
            //then
            assertAll(
                    () -> assertNotNull(result),
                    () -> assertEquals(userProfileDto, result),
                    () -> verify(userProfileRepository).save(userProfile)
            );
        }
    }
        @Test
        void testUpdateUserProfile_newUserProfile_Success() {
            //given
            UserProfile userProfile = UserProfileTestFactory.createUserProfile();
            UserProfileDto userProfileDto = UserProfileTestFactory.createUserProfileDtoFromEntity(userProfile);

            when(currentUserService.getCurrentUserId()).thenReturn(userProfile.getUserId());
            when(userProfileRepository.findUserProfileByUserId(userProfile.getUserId())).thenReturn(Optional.empty());
            when(userProfileMapper.toDto(userProfile)).thenReturn(userProfileDto);
            when(userProfileRepository.save(userProfile)).thenReturn(userProfile);
            when(userProfileMapper.toEntity(userProfileDto, userProfile.getUserId(), null)).thenReturn(userProfile);
            //when
            UserProfileDto result = userProfileService.updateUserProfile(userProfileDto);
            //then
            assertAll(
                    () -> assertNotNull(result),
                    () -> assertEquals(userProfileDto, result),
                    () -> verify(userProfileRepository).save(userProfile)
            );
        }

    @Nested
    @DisplayName("Delete user profile")
    public class userProfileServiceDelete {
        @Test
        void testDeleteUserProfile_Success() throws UserNotAuthenticatedException, UserProfileNotFoundException {
            //given
            UserProfile userProfile = UserProfileTestFactory.createUserProfile();
            UserProfileDto userProfileDto = UserProfileTestFactory.createUserProfileDtoFromEntity(userProfile);

            when(userProfileMapper.toDto(userProfile)).thenReturn(userProfileDto);
            when(currentUserService.getCurrentUserId()).thenReturn(userProfile.getUserId());
            when(userProfileRepository.findUserProfileByUserId(userProfile.getUserId())).thenReturn(Optional.of(userProfile));
            //when
            UserProfileDto result = userProfileService.deleteUserProfile();
            //then
            assertAll(
                    () -> assertNotNull(result),
                    () -> assertEquals(userProfileDto, result),
                    () -> verify(userProfileRepository).deleteById(userProfile.getId())
            );
        }


        @Test
        void testDeleteUserProfile_UserProfileDoesntExist_ThrowsUserProfileNotFoundException() {
            //given
            UserProfile userProfile = UserProfileTestFactory.createUserProfile();

            when(currentUserService.getCurrentUserId()).thenReturn(userProfile.getUserId());
            when(userProfileRepository.findUserProfileByUserId(userProfile.getUserId())).thenReturn(Optional.empty());
            //then
            assertThrows(UserProfileNotFoundException.class, () -> userProfileService.deleteUserProfile());
        }
    }
}