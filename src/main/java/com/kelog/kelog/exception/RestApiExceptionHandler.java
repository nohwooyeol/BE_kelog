package com.kelog.kelog.exception;


import com.kelog.kelog.response.ResponseDto;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestApiExceptionHandler {

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseDto<?> handleException(IllegalArgumentException ex){
        return ResponseDto.fail("BAD_REQUEST",ex.getMessage());
    }
}
