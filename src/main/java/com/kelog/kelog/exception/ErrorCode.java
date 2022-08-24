package com.kelog.kelog.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    OK(HttpStatus.OK, "0", "정상");


    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String erroemessage;

    ErrorCode(HttpStatus httpStatus, String errorCode, String erroemessage) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.erroemessage = erroemessage;
    }
}
