package com.yw.auth.global.exception;

import com.yw.auth.global.response.ErrorCode;
import lombok.Getter;

@Getter
public class UnauthorizedException extends RuntimeException {
    private final String message;
    private final String code;

    public UnauthorizedException(String message) {
        this.message = message;
        this.code = "UNAUTHORIZED";
    }

    public UnauthorizedException(String message, String code) {
        this.message = message;
        this.code = code;
    }

    public UnauthorizedException(ErrorCode errorCode) {
        this.message = errorCode.getMessage();
        this.code = errorCode.getCode();
    }
}
