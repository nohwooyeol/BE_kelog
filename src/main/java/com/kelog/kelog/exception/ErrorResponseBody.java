package com.kelog.kelog.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ErrorResponseBody {

    private String errorCode;
    private String errorMessage;
    private Object data;

    public ErrorResponseBody(ErrorCode errorCode) {
        this.errorCode = errorCode.getErrorCode();
        this.errorMessage = errorCode.getErroemessage();
    }

    public ErrorResponseBody(ErrorCode errorCode, Object data){
        this.errorCode = errorCode.getErrorCode();
        this.errorMessage = errorCode.getErroemessage();
        this.data = data;
    }
}
