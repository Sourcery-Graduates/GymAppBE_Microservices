package com.sourcery.gymapp.userProfile.service;

import com.sourcery.gymapp.userProfile.config.CurrentUserService;
import com.sourcery.gymapp.userProfile.exception.InvalidImageException;
import com.sourcery.gymapp.userProfile.exception.UserProfileNotFoundException;
import com.sourcery.gymapp.userProfile.factory.UserProfileTestFactory;
import com.sourcery.gymapp.userProfile.model.UserProfile;
import com.sourcery.gymapp.userProfile.repository.UserProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserPhotoServiceTest {

    @Mock
    private CurrentUserService currentUserService;

    @Mock
    private UserProfileRepository userProfileRepository;

    @Mock
    private S3Client s3Client;

    @InjectMocks
    private UserPhotoService userPhotoService;

    @Nested
    @DisplayName("Upload User Photo Tests")
    public class UploadUserPhotoTests {

        @BeforeEach
        void setUp() {
            ReflectionTestUtils.setField(userPhotoService, "maxFileSize", 1048576); // Set the maxFileSize to 1MB
        }

        @Test
        void uploadUserPhoto_shouldReturnImageIsEmpty() {
            MultipartFile multipartFile = new MockMultipartFile("file", new byte[0]);
            assertEquals("Image is empty", assertThrows(InvalidImageException.class,
                    () -> userPhotoService.uploadUserPhoto(multipartFile)).getMessage());
        }

        @Test
        void uploadUserPhoto_shouldReturnImageIsTooLarge() {
            MultipartFile multipartFile = new MockMultipartFile("file", new byte[1048577]);
            assertEquals("Image is too large", assertThrows(InvalidImageException.class,
                    () -> userPhotoService.uploadUserPhoto(multipartFile)).getMessage());
        }

        @Test
        void uploadUserPhoto_shouldReturnInvalidImageType() {
            MultipartFile multipartFile = new MockMultipartFile("file", "test.pdf", "application/pdf", new byte[1]);

            assertEquals("Invalid image type", assertThrows(InvalidImageException.class,
                    () -> userPhotoService.uploadUserPhoto(multipartFile)).getMessage());
        }

        @Test
        void uploadUserPhoto_shouldReturnUserProfileNotFound() {
            MultipartFile multipartFile = new MockMultipartFile("file", "image.jpg", "image/jpg", new byte[1]);
            UUID userId = UUID.randomUUID();
            when(currentUserService.getCurrentUserId()).thenReturn(userId);
            when(userProfileRepository.findUserProfileByUserId(userId)).thenReturn(Optional.empty());

            assertThrows(UserProfileNotFoundException.class,
                    () -> userPhotoService.uploadUserPhoto(multipartFile));
        }

        @Test
        void uploadUserPhoto_shouldPutNewObject() {
            MultipartFile multipartFile = new MockMultipartFile("file", "image.jpg", "image/jpg", new byte[1]);
            UUID userId = UUID.randomUUID();
            UserProfile userProfile = UserProfileTestFactory
                    .createUserProfile(
                            userId,
                            "johndoe",
                            "John",
                            "Doe",
                            "bio",
                            null,
                            "Warsaw",
                            null
                    );

            when(currentUserService.getCurrentUserId()).thenReturn(userId);
            when(userProfileRepository.findUserProfileByUserId(userId)).thenReturn(Optional.of(userProfile));
            when(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class)))
                    .thenReturn(PutObjectResponse.builder().build());

            userPhotoService.uploadUserPhoto(multipartFile);
            verify(s3Client, times(0)).deleteObject(any(DeleteObjectRequest.class));
            verify(s3Client, times(1)).putObject(any(PutObjectRequest.class), any(RequestBody.class));
            verify(userProfileRepository, times(1)).save(userProfile);
        }

        @Test
        void uploadUserPhoto_shouldDeletePreviousObjectAndPutNew() {
            MultipartFile multipartFile = new MockMultipartFile("file", "image.jpg", "image/jpg", new byte[1]);
            UUID userId = UUID.randomUUID();
            String objectKey = userId + "/user-photo-" + UUID.randomUUID();
            String avatarUrl = "https://%s.s3.%s.amazonaws.com/%s".formatted("bucket", "region", objectKey);
            UserProfile userProfile = UserProfileTestFactory
                    .createUserProfile(
                            userId,
                            "johndoe",
                            "John",
                            "Doe",
                            "bio",
                            avatarUrl,
                            "Warsaw",
                            null
                    );

            when(currentUserService.getCurrentUserId()).thenReturn(userId);
            when(userProfileRepository.findUserProfileByUserId(userId)).thenReturn(Optional.of(userProfile));
            when(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class)))
                    .thenReturn(PutObjectResponse.builder().build());

            userPhotoService.uploadUserPhoto(multipartFile);
            verify(s3Client, times(1)).deleteObject(any(DeleteObjectRequest.class));
            verify(s3Client, times(1)).putObject(any(PutObjectRequest.class), any(RequestBody.class));
            verify(userProfileRepository, times(1)).save(userProfile);
        }
    }
}