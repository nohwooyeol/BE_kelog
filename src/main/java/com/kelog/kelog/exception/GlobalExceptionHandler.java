package com.kelog.kelog.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//Global error controller
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = { CustomException.class })
    public ResponseEntity<Object> handleApiRequestException(CustomException ex) {
        String errorCode = ex.getErrorCode().getErrorCode();
        String errorMessage = ex.getErrorCode().getErroemessage();
        ErrorResponseBody errorResponseBody = new ErrorResponseBody();
        errorResponseBody.setErrorCode(errorCode);
        errorResponseBody.setErrorMessage(errorMessage);

        System.out.println("ERR :" + errorCode + " , " + errorMessage);  //Log용

        return new ResponseEntity(
                errorResponseBody,
                ex.getErrorCode().getHttpStatus()
        );
    }
}

