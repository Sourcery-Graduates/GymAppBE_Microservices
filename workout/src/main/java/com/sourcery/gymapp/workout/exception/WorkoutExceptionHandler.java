package com.sourcery.gymapp.workout.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

@ControllerAdvice(basePackages = "com.sourcery.gymapp.backend.workout.controller")
@Slf4j
public class WorkoutExceptionHandler {

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<FieldErrorResponse> handleValidationException(
            HandlerMethodValidationException ex) {
        log.error("HandlerMethodValidationException caught: {}", ex.getAllErrors(), ex);

        List<FieldResponse> fields = ex.getAllErrors().stream()
                .filter(error -> error instanceof FieldError)
                .map(error -> {
                    String fieldName = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();
                    return new FieldResponse(fieldName, errorMessage);
                })
                .toList();

        FieldErrorResponse response = new FieldErrorResponse(
                "Request validation error",
                ErrorCode.VALIDATION_ERROR,
                fields);

        return new ResponseEntity<>(response, ex.getStatusCode());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(RoutineNotFoundException ex) {
        log.error("UserNotFoundException caught: {}", ex.getMessage(), ex);

        ErrorResponse response = new ErrorResponse(ex.getMessage(), ex.getCode());
        return new ResponseEntity<>(response, ex.getStatus());
    }

    @ExceptionHandler(RoutineNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRoutineNotFoundException(
            RoutineNotFoundException ex) {
        log.error("RoutineNotFoundException caught: {}", ex.getMessage(), ex);

        ErrorResponse response = new ErrorResponse(ex.getMessage(), ex.getCode());
        return new ResponseEntity<>(response, ex.getStatus());
    }

    @ExceptionHandler(WorkoutNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleWorkoutNotFoundException(
            WorkoutNotFoundException ex) {
        log.error("WorkoutNotFoundException caught: {}", ex.getMessage(), ex);

        ErrorResponse response = new ErrorResponse(ex.getMessage(), ex.getCode());
        return new ResponseEntity<>(response, ex.getStatus());
    }

    @ExceptionHandler(WorkoutRuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRoutineRuntimeException(
            WorkoutRuntimeException ex) {
        log.error("RoutineRuntimeException caught: {}", ex.getMessage(), ex);
        ErrorResponse response = new ErrorResponse(ex.getMessage(), ex.getCode());
        return new ResponseEntity<>(response, ex.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericWorkoutException(Exception ex) {
        log.error("Unexpected workout module error occurred: {}", ex.getMessage(), ex);
        ErrorResponse response = new ErrorResponse("Internal server error",
                ErrorCode.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<FieldErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex) {
        log.error("MethodArgumentNotValidException caught: {}",
                ex.getBindingResult().getAllErrors(), ex);

        List<FieldResponse> fields = ex.getBindingResult().getAllErrors().stream()
                .filter(error -> error instanceof FieldError)
                .map(error -> {
                    String fieldName = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();
                    return new FieldResponse(fieldName, errorMessage);
                })
                .toList();

        FieldErrorResponse response = new FieldErrorResponse(
                "Request validation error",
                ErrorCode.VALIDATION_ERROR,
                fields);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LikeNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleLikeNotFoundException(LikeNotFoundException ex) {
        log.error("LikeNotFoundException caught: {}", ex.getMessage(), ex);

        ErrorResponse response = new ErrorResponse(ex.getMessage(), ex.getCode());
        return new ResponseEntity<>(response, ex.getStatus());
    }

    @ExceptionHandler(LikeAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleLikeAlreadyExistsException(LikeAlreadyExistsException ex) {
        log.error("LikeAlreadyExistsException caught: {}", ex.getMessage(), ex);

        ErrorResponse response = new ErrorResponse(ex.getMessage(), ex.getCode());
        return new ResponseEntity<>(response, ex.getStatus());
    }

    @ExceptionHandler(WrongOrderException.class)
    public ResponseEntity<ErrorResponse> handleWrongOrderException(WrongOrderException ex) {
        log.error("WrongOrderException caught: {}", ex.getMessage(), ex);

        ErrorResponse response = new ErrorResponse(ex.getMessage(), ex.getCode());
        return new ResponseEntity<>(response, ex.getStatus());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleWrongArgumentTypeException(MethodArgumentTypeMismatchException ex) {
        log.error("{} caught: {}", ex.getClass().getName(), ex.getMessage(), ex);

        ErrorResponse response = new ErrorResponse("%s: %s".formatted(ex.getName(), ex.getMessage()), ErrorCode.METHOD_ARGUMENT_TYPE_MISMATCH);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(UserIdHeaderNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserIdHeaderNotFoundException(UserIdHeaderNotFoundException ex) {
        log.error("UserIdHeaderNotFoundException caught: {}", ex.getMessage(), ex);
        ErrorResponse response = new ErrorResponse(ex.getMessage(), ex.getCode());
        return new ResponseEntity<>(response, ex.getStatus());
    }
}
