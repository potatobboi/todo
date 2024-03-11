package com.sparta.todo.common.exception;

import com.sparta.todo.common.enumeration.ErrorCode;
import org.springframework.http.HttpStatus;

public class InvalidInputException extends RuntimeException {

    private final HttpStatus status;

    public InvalidInputException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.status = errorCode.getStatus();
    }
}
