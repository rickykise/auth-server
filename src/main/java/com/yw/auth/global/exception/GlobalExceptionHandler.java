package com.yw.auth.global.exception;

import com.yw.auth.global.response.CommonResponse;
import com.yw.auth.global.response.ErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // 예상하지 못한 error는 공통 Error Message
    @ResponseBody
    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResponse<Object> handleException(Exception exception) {
        log.error(exception.getMessage(), exception);
        return CommonResponse.fail(ErrorCode.COMMON_ILLEGAL_STATUS.getMessage(), ErrorCode.COMMON_ILLEGAL_STATUS.getCode());
    }

    // 비즈니스 로직상 예상 할 수 있는 에러는 커스터마이징 Error Message
    @ResponseBody
    @ExceptionHandler({BusinessException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResponse<Object> businessException(Exception exception) throws JsonProcessingException {
        log.error(exception.getMessage());
        return CommonResponse.fail(exception.getMessage(), ErrorCode.COMMON_ILLEGAL_STATUS.getCode(), CommonResponse.ErrorType.ERROR);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public CommonResponse<Object> handleMethodArgumentNotValidException(
            Exception exception
    ) {
        log.error(exception.getMessage(), exception);
        return CommonResponse.fail(ErrorCode.EMPTY_TOKEN.getMessage(), ErrorCode.EMPTY_TOKEN.getCode(), CommonResponse.ErrorType.ERROR);
    }
}
