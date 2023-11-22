package com.example.peernow360.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RestApiException extends RuntimeException{

    /*
     * 체크 예외가 아닌 언체크 예외를 상속받도록 한 이유가 있다.
     * 왜냐하면 일반적인 비지니스 로직들은 따로 catch해서 처리할 것이 없므로
     * 만약 체크 예외로 한다면 불필요하게 throws가 전파될 것이기 때문이다.
     */
    private final ErrorCode errorCode;

}
