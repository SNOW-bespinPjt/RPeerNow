package com.example.peernow360.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ReviewErrorCode{

    ALREADY_EVALUATE(409, "이미 평가를 완료하였습니다.");

    private final int status;
    private final String message;

}