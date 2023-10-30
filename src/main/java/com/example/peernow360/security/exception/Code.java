package com.example.peernow360.security.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor // 여기에 필드에 쓴 모든생성자만 만들어줌
public enum Code {

    UNKNOWN_ERROR(401),     //존재하지 않는 토큰
    WRONG_TYPE_TOKEN(402),  //변조된 토큰
    EXPIRED_TOKEN(403),     //만료된 토큰
    UNSUPPORTED_TOKEN(404); //변조된 토큰

    private int code;

}
