package com.example.peernow360.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Log4j2
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{

    /*
     * RestApiException 예외와 @Valid에 의한 유효성 검증에 실패했을 때 발생하는 IllegalArgumentException 예외
     * 잘못된 파라미터를 넘겼을 경우 발생하는 IllegalArgumentException 에러를 처리
     */

    @ExceptionHandler(RestApiException.class)
    public ResponseEntity<Object> handleCustomException(RestApiException e) {
        ErrorCode errorCode = e.getErrorCode();

        return handleExceptionInternal(errorCode);

    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException e) {
        log.warn("handlerIllegalArgument", e);

        ErrorCode errorCode = CommonErrorCode.INVALID_PARAMETER;

        return handleExceptionInternal(errorCode, e.getMessage());

    }



    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAllException(Exception ex) {
        log.warn("handleAllException", ex);

        ErrorCode errorCode = CommonErrorCode.INTERNAL_SERVER_ERROR;
        return handleExceptionInternal(errorCode);
        
    }

    @ExceptionHandler({CustomException.class})
    protected ResponseEntity<Object> handleCustomException(CustomException ex) {

        ReviewErrorCode reviewErrorCode = ReviewErrorCode.ALREADY_EVALUATE;

        return handleExceptionInternal(reviewErrorCode);
    }

    public ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {

        log.warn("handleIllegalArgument", e);

        ErrorCode errorCode = CommonErrorCode.INVALID_PARAMETER;

        return handleExceptionInternal(e, errorCode);

    }

    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode) {

        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(makeErrorResponse(errorCode));
        
    }

    private ResponseEntity<Object> handleExceptionInternal(ReviewErrorCode reviewErrorCode) {

        return ResponseEntity.status(reviewErrorCode.getStatus())
                .body(makeErrorResponse(reviewErrorCode));

    }


    private Object makeErrorResponse(ErrorCode errorCode) {

        return ErrorResponse.builder()
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .build();

    }

    private Object makeErrorResponse(ReviewErrorCode reviewErrorCode) {

        return ErrorResponse.builder()
                .code(reviewErrorCode.getStatus())
                .message(reviewErrorCode.getMessage())
                .build();

    }

    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode, String message) {

        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(makeErrorResponse(errorCode, message));

    }

    private ErrorResponse makeErrorResponse(ErrorCode errorCode, String message) {

        return ErrorResponse.builder()
                .code(errorCode.name())
                .message(message)
                .build();

    }

    private ResponseEntity<Object> handleExceptionInternal(BindException e , ErrorCode errorCode) {

        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(makeErrorResponse(e, errorCode));

    }

    private ErrorResponse makeErrorResponse(BindException e, ErrorCode errorCode) {

        List<ErrorResponse.ValidationError> validationErrorList = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(ErrorResponse.ValidationError::of)
                .collect(Collectors.toList());

        return ErrorResponse.builder()
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .errors(validationErrorList)
                .build();

    }

//    @ExceptionHandler(RestApiException.class)
//    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(RestApiException e) {
//        log.warn("handleMethodArgumentTypeMismatch ", e);
//
//        ErrorCode errorCode = CommonErrorCode.INVALID_PARAMETER;
//
//        return handleExceptionInternal(errorCode, e.getMessage());
//
//    }


}


