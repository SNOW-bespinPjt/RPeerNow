package com.example.peernow360.security.exception;//package com.btc.jwtlogin.exception;
//
//import lombok.extern.log4j.Log4j2;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//import org.springframework.http.ResponseEntity;
//
//@Log4j2
//@RestControllerAdvice
//public class ErrorControllerAdvice {
//
//    @ExceptionHandler(value = UsernameNotFoundException.class)
//    protected ResponseEntity<ErrorResponse> handleUserNameNotFoundException(){
//        ErrorResponse response = new ErrorResponse(Code.BOARD_NOT_FOUND);
//        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//
//    }
//
//}
