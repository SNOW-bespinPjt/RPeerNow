package com.example.peernow360.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CustomException extends RuntimeException{

    private final ReviewErrorCode reviewErrorCode;

}
