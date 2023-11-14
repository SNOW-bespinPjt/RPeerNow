package com.example.peernow360.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor // 여기에 필드에 쓴 모든생성자만 만들어줌
public enum Code {

    VALID_TOKEN(200, "Token is valid"),     //유효한 토큰
    INVALID_TOKEN(101, "Invalid JWT Token"),  //변조된 토큰
    EXPIRED_TOKEN(102, "Expired JWT Token"),     //만료된 토큰
    UNSUPPORTED_TOKEN(103, "Unsupported JWT Token"), //변조된 토큰
    INVALID_SIGNATURE_TOKEN(104, "Invalid Signature JWT Token"); //변조된 토큰

    private int code;
    private String message;

}
