package com.example.peernow360.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/*
 * 필요한 권한이 존재하지 않는 경우에 403 Forbidden 에러를 리턴하는 class
 */

@Component
@Log4j2
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.info("==============================================");
        log.info("fail and call CustomAccessDeniedHandler");
        log.error("AccessDeniedException", accessDeniedException);
        log.info("==============================================");

        response.sendRedirect("/");
    }

}
