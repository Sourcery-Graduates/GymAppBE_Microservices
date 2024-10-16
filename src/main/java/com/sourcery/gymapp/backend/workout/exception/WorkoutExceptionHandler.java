package com.sourcery.gymapp.backend.workout.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackages = "com.sourcery.gymapp.backend.workout.controller")
@Slf4j
public class WorkoutExceptionHandler {

    @ExceptionHandler(RoutineNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRoutineNotFoundException(RoutineNotFoundException ex) {
        log.error("RoutineNotFoundException caught: {}", ex.getMessage(), ex);

        ErrorResponse response = new ErrorResponse(ex.getMessage(), ex.getCode(), null);
        return new ResponseEntity<>(response, ex.getStatus());
    }

    @ExceptionHandler(RoutineRuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRoutineRuntimeException(
            RoutineRuntimeException ex) {
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
