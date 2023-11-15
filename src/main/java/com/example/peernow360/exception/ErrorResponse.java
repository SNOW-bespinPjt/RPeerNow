package com.example.peernow360.exception;

import com.amazonaws.services.ec2.model.ValidationError;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

import java.util.List;

@Getter
@RequiredArgsConstructor
@Builder
public class ErrorResponse {

    private final Object code;
    private final String message;


    /*
     *  만약 errors가 없다면 응답으로 내려가지 않도록 @JsonInclude 어노테이션을 추가
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final List<ValidationError> errors;

    @Getter
    @Builder
    @RequiredArgsConstructor
    public static class ValidationError {

        /*
         * @Valid를 사용했을 때 에러가 발생한 경우
         * 어느 필드에서 에러가 발생했는지 응답을 위한 ValidationError를 내부 정적 클래스로 추가
         */

        private final String field;
        private final String message;

        static ValidationError of(final FieldError fieldError) {

            return ValidationError.builder()
                    .field(fieldError.getField())
                    .message(fieldError.getDefaultMessage())
                    .build();

        }

    }

}
