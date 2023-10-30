package com.example.peernow360.security;

import com.example.peernow360.dto.ResponseDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Component
@Log4j2
@RequiredArgsConstructor
public class JWTtokenFilter extends OncePerRequestFilter {

    private final JWTtokenProvider jwTtokenProvider;

    @Value("${jwt.HttpHeaderValue}")
    private String HttpHeaderValue;

    /*
     * 실제로 Spring Security 필터 체인에서 자동으로 호출됩니다. Spring Security는
     * 요청을 처리하기 전에 여러 개의 필터를 사용하여 요청을 처리하며, 이 필터 체인의 일부로서 JWTtokenFilter도 사용
     * 1. 클라이언트로부터의 HTTP 요청이 서버에 도달하면, Spring Security 필터 체인이 시작됩니다.
     * 2. JWTtokenFilter가 이 필터 체인 중 하나로 등록되어 있으며, 요청이 이 필터에 도달하면 doFilter 메서드가 호출됩니다.
     * 3. doFilter 메서드 내에서는 클라이언트가 요청한 내용을 확인하고, 요청에서 JWT 토큰을 추출하여 유효성을 검사합니다.
     * 4. JWT 토큰이 유효하면, 해당
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        log.info("[JWTtokenFilter]가즈아~ doFilter");

        String bearerToken = request.getHeader(HttpHeaderValue);
        log.info("what is token:" + bearerToken);

        // Header의 Authorization 값이 비어있으면 => Jwt Token을 전송하지 않음 => 로그인 하지 않음
        if (!StringUtils.hasText(bearerToken)) {
            log.info("토큰이 존재하지 않습니다.");
            chain.doFilter(request, response);
            return;

        }

        // Header의 Authorization 값이 'Bearer '로 시작하지 않으면 => 잘못된 토큰
        if (!bearerToken.startsWith("Bearer ")) {
            log.info("인증이 잘못된 토큰입니다.");
            chain.doFilter(request, response);
            return;

        }

        // bearer 헤더 제거 작업.
        String accessToken = bearerToken.substring(7);

        /*
         * access token 검증
         */
        if(jwTtokenProvider.validateToken(accessToken)) {
            log.info("access token 검증 ------- 토큰 정보 보유 ------ 정보 유효기간 확인 -----");

            Authentication authentication = jwTtokenProvider.selectAuthority(accessToken);
            log.info("authentication: " + authentication);

            //인증 정보를 Thread Local에 저장, 권한 부여
            SecurityContextHolder.getContext().setAuthentication(authentication);

            log.info("검증 완료");

        }

        ResponseEntity<Map<String,Object>> validationResult = jwTtokenProvider.validateTokenAndReturnMessage(accessToken);

        request.setAttribute("tokenValidationResult", validationResult);

        chain.doFilter(request, response);

    }

}
