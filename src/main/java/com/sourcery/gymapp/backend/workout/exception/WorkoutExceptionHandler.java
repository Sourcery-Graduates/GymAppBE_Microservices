package com.sourcery.gymapp.backend.workout.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.List;

@ControllerAdvice(basePackages = "com.sourcery.gymapp.backend.workout.controller")
@Slf4j
public class WorkoutExceptionHandler {

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(HandlerMethodValidationException ex) {
        log.error("HandlerMethodValidationException caught: {}", ex.getAllErrors(), ex);

        List<FieldResponse> fields = ex.getAllErrors().stream()
                .filter(error -> error instanceof FieldError)
                .map(error -> {
                    String fieldName = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();
                    return new FieldResponse(fieldName, errorMessage);
                })
                .toList();

        ErrorResponse response = new ErrorResponse(
                "Request validation error",
                ErrorCode.VALIDATION_ERROR,
                fields);

        return new ResponseEntity<>(response, ex.getStatusCode());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(RoutineNotFoundException ex) {
        log.error("UserNotFoundException caught: {}", ex.getMessage(), ex);

        ErrorResponse response = new ErrorResponse(ex.getMessage(), ex.getCode(), null);
        return new ResponseEntity<>(response, ex.getStatus());
    }

    @ExceptionHandler(RoutineNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRoutineNotFoundException(RoutineNotFoundException ex) {
        log.error("RoutineNotFoundException caught: {}", ex.getMessage(), ex);

        ErrorResponse response = new ErrorResponse(ex.getMessage(), ex.getCode(), null);
        return new ResponseEntity<>(response, ex.getStatus());
    }

    @ExceptionHandler(WorkoutRuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRoutineRuntimeException(
            WorkoutRuntimeException ex) {
        log.error("RoutineRuntimeException caught: {}", ex.getMessage(), ex);
        ErrorResponse response = new ErrorResponse(ex.getMessage(), ex.getCode(), null);
        return new ResponseEntity<>(response, ex.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericWorkoutException(Exception ex) {
        log.error("Unexpected workout module error occurred: {}", ex.getMessage(), ex);
        ErrorResponse response = new ErrorResponse("Internal server error",
                ErrorCode.INTERNAL_SERVER_ERROR,
                null);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
