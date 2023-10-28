package com.example.peernow360.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonResponse {

    private boolean success;
    private int code;
    private String message;
}