package com.example.peernow360.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode{

    inactive_user(HttpStatus.FORBIDDEN, "user is inactive"),
    ;

    private final HttpStatus httpStatus;
    private final String message;

}
