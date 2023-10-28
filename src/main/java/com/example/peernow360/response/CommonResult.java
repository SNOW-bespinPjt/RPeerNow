package com.example.peernow360.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommonResult {

    SUCCESS(0, "성공"),
    FAIL(-1, "실패");

    private int code;
    private String message;
}
