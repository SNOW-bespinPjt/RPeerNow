package com.example.peernow360.security.exception;//package com.btc.jwtlogin.exception;
//
//import io.jsonwebtoken.ExpiredJwtException;
//import io.jsonwebtoken.MalformedJwtException;
//import io.jsonwebtoken.SignatureException;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//@Log4j2
//@RequiredArgsConstructor
//@Component
//public class JwtExceptionFilter extends OncePerRequestFilter {
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        response.setCharacterEncoding("utf-8");
//
//        try{
//            filterChain.doFilter(request, response);
//
//        } catch (ExpiredJwtException e){
//            //만료 에러
//            request.setAttribute("exception", Code.EXPIRED_TOKEN.getCode());
//
//        } catch (MalformedJwtException e){
//            //변조 에러
//            request.setAttribute("exception", Code.WRONG_TYPE_TOKEN.getCode());
//
//        } catch (SignatureException e){
//            //형식, 길이 에러
//            request.setAttribute("exception", Code.WRONG_TYPE_TOKEN.getCode());
//        }
//
//        filterChain.doFilter(request, response);
//
//    }
//}
