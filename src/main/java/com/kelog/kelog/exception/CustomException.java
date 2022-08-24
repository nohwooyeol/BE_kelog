package com.kelog.kelog.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException{

    private ErrorCode errorCode;

    public CustomException(ErrorCode errorCode){
        super(errorCode.getErrorCode());
        this.errorCode=errorCode;
    }
}
