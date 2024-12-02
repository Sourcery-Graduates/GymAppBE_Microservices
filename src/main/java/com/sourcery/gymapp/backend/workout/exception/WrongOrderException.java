package com.sourcery.gymapp.backend.workout.exception;

import org.springframework.http.HttpStatus;

public class WrongOrderException extends WorkoutRuntimeException {

  public WrongOrderException() {
    super(
            "Order numbers must start from 1 and increment sequentially",
            ErrorCode.VALIDATION_ERROR,
            HttpStatus.BAD_REQUEST);
    }
}
