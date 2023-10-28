package com.example.peernow360.security;


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
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

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

    @JsonIgnore
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        sendErrorResponse(response, "인증에 실패하였습니다");

        log.info("fail and call CustomAccessHandler");
        log.info("request:{}",request);
        log.info("authException: " + authException);

        response.sendRedirect("/");


//        String exception = request.getAttribute("exception").toString();
//
//        if(!StringUtils.hasText(exception)) {
//            setResponse(response, Code.UNKNOWN_ERROR);
//        }
//        //잘못된 타입의 토큰인 경우
//        else if(exception.equals(Code.WRONG_TYPE_TOKEN.getCode()+"")) {
//
//            setResponse(response, Code.WRONG_TYPE_TOKEN);
//        }
//        //토큰 만료된 경우
//        else if(exception.equals(Code.EXPIRED_TOKEN.getCode()+"")) {
//
//            setResponse(response, Code.EXPIRED_TOKEN);
//        }
//        //지원되지 않는 토큰인 경우
//        else if(exception.equals(Code.UNSUPPORTED_TOKEN.getCode()+"")) {
//
//            setResponse(response, Code.UNSUPPORTED_TOKEN);
//        }
//        else {
//            setResponse(response, Code.ACCESS_DENIED);
//        }
//    }
//    //한글 출력을 위해 getWriter() 사용
//    private void setResponse(HttpServletResponse response, Code code) throws IOException {
//        response.setContentType("application/json;charset=UTF-8");
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//
//        JSONObject responseJson = new JSONObject();
//        responseJson.put("message", code.getMessage());
//        responseJson.put("code", code.getCode());
//
//        response.getWriter().print(responseJson);
//
    }

    /*
     * jwt 예외처리 응답
     * @Param message 예외 메세지
     */

    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setCharacterEncoding("utf-8");                       // 응답의 문자 인코딩을 UTF-8로 설정합니다.
        response.setStatus(HttpStatus.UNAUTHORIZED.value());          // 응답의 HTTP 상태 코드를 UNAUTHORIZED(401)로 설정합니다.
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);    // 응답의 컨텐츠 타입을 JSON으로 설정.
//        response.getWriter().write(objectMapper.writeValueAsString(  // objectMapper를 사용하여 Response 객체를 JSON 문자열로 변환하여 클라이언트에 응답을 보낸다.
//                        Response.builder()
//                        .status(HttpStatus.FORBIDDEN.value())
//                        .message(message)
//                        .build()));

    }
}

