package com.sourcery.gymapp.userProfile.service;

import com.sourcery.gymapp.userProfile.exception.InvalidImageException;
import com.sourcery.gymapp.userProfile.exception.S3PhotoUploadException;
import com.sourcery.gymapp.userProfile.exception.UserProfileNotFoundException;
import com.sourcery.gymapp.userProfile.model.UserProfile;
import com.sourcery.gymapp.userProfile.repository.UserProfileRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserPhotoService {
    private final CurrentUserService currentUserService;
    private final UserProfileRepository userProfileRepository;
    private final S3Client s3Client;

    @Value("${aws.s3.region}")
    private String awsRegion;

    @Value("${aws.s3.bucket}")
    private String awsBucket;

    @Value("${spring.servlet.multipart.max-file-size}")
    private int maxFileSize;

    @Transactional
    public void uploadUserPhoto(MultipartFile image) {
        validateImage(image);

        UUID currentUserId = currentUserService.getCurrentUserId();
        String bucket = awsBucket;
        String region = awsRegion;

        try {
            UserProfile userProfile = userProfileRepository.findUserProfileByUserId(currentUserId)
                    .orElseThrow(() -> new UserProfileNotFoundException(currentUserId));

            String oldAvatarUrl = userProfile.getAvatarUrl();
            String avatarIdentifier = "user-avatar";
            String newObjectKey = currentUserId.toString() + '/' + avatarIdentifier + '-' + UUID.randomUUID();

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                   .bucket(bucket)
                   .key(newObjectKey)
                   .build();

            RequestBody requestBody = RequestBody.fromInputStream(image.getInputStream(), image.getSize());

            s3Client.putObject(putObjectRequest, requestBody);

            String newObjectUrl = "https://%s.s3.%s.amazonaws.com/%s".formatted(bucket, region, newObjectKey);

            updateAvatarUrl(userProfile, newObjectUrl);

            if (oldAvatarUrl != null) {
                String oldObjectKey = decodeObjectKeyFromAvatarUrl(oldAvatarUrl);
                DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                        .bucket(bucket)
                        .key(oldObjectKey)
                        .build();

                s3Client.deleteObject(deleteObjectRequest);
            }
        } catch (IOException e) {
              throw new S3PhotoUploadException();
        }
    }

    private void validateImage(MultipartFile image) {
        if (image.isEmpty()) {
            throw new InvalidImageException("Image is empty", HttpStatus.BAD_REQUEST);
        }
        if (image.getSize() > maxFileSize) {
            throw new InvalidImageException("Image is too large", HttpStatus.PAYLOAD_TOO_LARGE);
        }

        List<String> allowedContentTypes = List.of("image/jpeg", "image/png", "image/jpg", "image/gif");

        if (!allowedContentTypes.contains(image.getContentType())) {
            throw new InvalidImageException("Invalid image type", HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        }
    }

    /**
     * @param avatarUrl - url of the avatar
     * The object key is the last 85 characters of the url
     * @return object key
     */
    private String decodeObjectKeyFromAvatarUrl(String avatarUrl) {
        int urlSuffixLength = 85;

        return avatarUrl.substring(avatarUrl.length() - urlSuffixLength);
    }

    private void updateAvatarUrl(UserProfile userProfile, String url) {
        userProfile.setAvatarUrl(url);
        userProfileRepository.save(userProfile);
    }
}
