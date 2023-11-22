package com.example.peernow360.security;


import com.example.peernow360.dto.ResponseDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.stream.DoubleStream;

import static org.apache.coyote.Response.*;

/*
 * 유효한 자격증명을 제공하지 않고 접근하려 할때 401 Unauthorized 에러를 리턴하는 class
 */

@Component
@Log4j2
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Value("${jwt.EXCEPTION}")
    private String exception;

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        ResponseEntity<Map<String, Object>> validationResult = (ResponseEntity<Map<String, Object>>) request.getAttribute("tokenValidationResult");

        if(validationResult != null) {
            response.setStatus(validationResult.getStatusCodeValue());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(objectMapper.writeValueAsString(validationResult.getBody()));

        } else {
            //refresh Token 미 존재시 403 에러 출력 이는 프론트에서 redirect로 로그인창으로 가게끔 하기
            sendErrorResponse(response, "인증에 실패하였습니다");

        }

    }

    /*
     * jwt 예외처리 응답
     * @Param message 예외 메세지
     */
    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setCharacterEncoding("utf-8");                       // 응답의 문자 인코딩을 UTF-8로 설정합니다.
        response.setStatus(HttpStatus.UNAUTHORIZED.value());          // 응답의 HTTP 상태 코드를 UNAUTHORIZED(401)로 설정합니다.
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);    // 응답의 컨텐츠 타입을 JSON으로 설정.
        response.getWriter().write(objectMapper.writeValueAsString(  // objectMapper를 사용하여 Response 객체를 JSON 문자열로 변환하여 클라이언트에 응답을 보낸다.
                        ResponseDto.builder()
                        .status(HttpStatus.FORBIDDEN.value())
                        .message(message)
                        .build()));

    }

}

