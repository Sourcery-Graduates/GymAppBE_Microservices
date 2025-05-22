package com.sourcery.gymapp.userProfile.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice(basePackages = "com.sourcery.gymapp.backend.userProfile.controller")
@Slf4j
public class UserProfileExceptionHandler {

    @ExceptionHandler(UserProfileRuntimeException.class)
    public ResponseEntity<ErrorResponse> handleUserProfileRuntimeException(
            UserProfileRuntimeException ex) {
        log.error("UserProfileRuntimeException caught: {}", ex.getMessage(), ex);
        ErrorResponse response = new ErrorResponse(ex.getMessage(), ex.getCode(), null);
        return new ResponseEntity<>(response, ex.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        log.error("Unexpected error occurred: {}", ex.getMessage(), ex);
        ErrorResponse response = new ErrorResponse("Internal server error",
                ErrorCode.INTERNAL_SERVER_ERROR,
                null);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

//    @ExceptionHandler(AccessDeniedException.class)
//    public final ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex) {
//        return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
//    } TODO: not sure whats that used for

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex) {
        log.error("MethodArgumentNotValidException caught: {}", ex.getMessage(), ex);

        List<FieldResponse> fields = ex.getBindingResult().getFieldErrors().stream()
                .filter(fieldError -> fieldError.getDefaultMessage() != null)
                .map(fieldError -> new FieldResponse(
                        fieldError.getField(),
                        fieldError.getDefaultMessage()
                )).collect(Collectors.toList());

        ErrorResponse
                response = new ErrorResponse("Request validation error", ErrorCode.REQUEST_VALIDATION_ERROR, fields);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(S3Exception.class)
    public ResponseEntity<ErrorResponse> handleS3Exception(S3Exception ex) {
        log.error("S3Exception caught: {}", ex.awsErrorDetails());
        ErrorResponse response = new ErrorResponse(ex.awsErrorDetails().errorMessage(),
                ErrorCode.AWS_S3_ERROR,
                null);

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(S3PhotoUploadException.class)
    public ResponseEntity<ErrorResponse> handleS3PhotoUploadException(S3PhotoUploadException ex) {
        log.error("S3PhotoUploadException caught: {}", ex.getMessage(), ex);
        ErrorResponse response = new ErrorResponse(ex.getMessage(), ex.getCode(), null);
        return new ResponseEntity<>(response, ex.getStatus());
    }
}