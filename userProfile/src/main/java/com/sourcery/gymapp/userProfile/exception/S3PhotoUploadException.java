package com.sourcery.gymapp.userProfile.exception;

import org.springframework.http.HttpStatus;

public class S3PhotoUploadException extends UserProfileRuntimeException {
    public S3PhotoUploadException() {
        super("Failed to upload photo to S3",
                ErrorCode.S3_PHOTO_UPLOAD_FAILED,
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
