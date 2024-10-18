package com.sourcery.gymapp.backend.userProfile.service;

import com.sourcery.gymapp.backend.userProfile.dto.UserProfileDto;
import com.sourcery.gymapp.backend.userProfile.exception.UserNotFoundException;
import com.sourcery.gymapp.backend.userProfile.exception.UserProfileNotFoundException;
import com.sourcery.gymapp.backend.userProfile.factory.UserProfileTestFactory;
import com.sourcery.gymapp.backend.userProfile.mapper.UserProfileMapper;
import com.sourcery.gymapp.backend.userProfile.model.UserProfile;
import com.sourcery.gymapp.backend.userProfile.repository.UserProfileRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.AuditorAware;

import java.util.UUID;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;



@ExtendWith(MockitoExtension.class)
class UserProfileServiceTest {

    @Mock
    private AuditorAware<UUID> auditorAware;
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
        void testGetUserProfileSuccess() throws UserNotFoundException, UserProfileNotFoundException {
            //given
            UserProfile userProfile = UserProfileTestFactory.createUserProfile();
            UserProfileDto userProfileDto = UserProfileTestFactory.createUserProfileDtoFromEntity(userProfile);

            when(userProfileMapper.toDto(userProfile)).thenReturn(userProfileDto);
            when(auditorAware.getCurrentAuditor())
                    .thenReturn(Optional.of(userProfile.getUserId()));
            when(userProfileRepository.findUserProfileByUserId(userProfile.getUserId()))
                    .thenReturn(userProfile);

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
        void testGetUserProfile_UserDoesntExist_ThrowsUserNotFoundException() {
            //when
            when(auditorAware.getCurrentAuditor()).thenReturn(Optional.empty());
            //then
            assertThrows(UserNotFoundException.class, () -> userProfileService.getUserProfile());
        }

        @Test
        void testGetUserProfile_UserProfileDoesntExist_ThrowsUserNotFoundException() {
            //given
            UserProfile userProfile = UserProfileTestFactory.createUserProfile();

            when(auditorAware.getCurrentAuditor()).thenReturn(Optional.of(userProfile.getUserId()));
            when(userProfileRepository.findUserProfileByUserId(userProfile.getUserId())).thenReturn(null);
            //then
            assertThrows(UserProfileNotFoundException.class, () -> userProfileService.getUserProfile());
        }

 }

    @Nested
    @DisplayName("Update user profile")
    public class userProfileServiceUpdate {
        @Test
        void testUpdateUserProfile_Success() {
            //given
            UserProfile userProfile = UserProfileTestFactory.createUserProfile();
            UserProfileDto userProfileDto = UserProfileTestFactory.createUserProfileDtoFromEntity(userProfile);

            when(userProfileMapper.toDto(userProfile)).thenReturn(userProfileDto);
            when(userProfileRepository.save(userProfile)).thenReturn(userProfile);
            when(userProfileMapper.toEntity(userProfileDto)).thenReturn(userProfile);
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

    @Nested
    @DisplayName("Delete user profile")
    public class userProfileServiceDelete {
        @Test
        void testDeleteUserProfile_Success() throws UserNotFoundException, UserProfileNotFoundException {
            //given
            UserProfile userProfile = UserProfileTestFactory.createUserProfile();
            UserProfileDto userProfileDto = UserProfileTestFactory.createUserProfileDtoFromEntity(userProfile);

            when(userProfileMapper.toDto(userProfile)).thenReturn(userProfileDto);
            when(auditorAware.getCurrentAuditor()).thenReturn(Optional.of(userProfile.getUserId()));
            when(userProfileRepository.findUserProfileByUserId(userProfile.getUserId())).thenReturn(userProfile);
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
        void testDeleteUserProfile_ThrowsUserNotFoundException() {
            //given
            when(auditorAware.getCurrentAuditor()).thenReturn(Optional.empty());
            //then
            assertThrows(UserNotFoundException.class, () -> userProfileService.deleteUserProfile());
        }

        @Test
        void testDeleteUserProfile_ThrowsUserProfileNotFoundException() {
            //given
            UserProfile userProfile = UserProfileTestFactory.createUserProfile();

            when(auditorAware.getCurrentAuditor()).thenReturn(Optional.of(userProfile.getUserId()));
            when(userProfileRepository.findUserProfileByUserId(userProfile.getUserId())).thenReturn(null);
            //then
            assertThrows(UserProfileNotFoundException.class, () -> userProfileService.deleteUserProfile());
        }
    }
}