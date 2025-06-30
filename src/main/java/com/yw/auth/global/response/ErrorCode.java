package com.yw.auth.global.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // Common errors
    COMMON_SYSTEM_ERROR("COM_001", "일시적인 오류가 발생했습니다. 잠시 후 다시 시도해주세요."),
    COMMON_INVALID_PARAMETER("COM_002", "요청한 값이 올바르지 않습니다."),
    COMMON_ENTITY_NOT_FOUND("COM_003", "존재하지 않는 엔티티입니다."),
    COMMON_BAD_REQUEST("COM_004", "잘못된 요청값입니다."),
    COMMON_ILLEGAL_STATUS("COM_005", "잘못된 상태값입니다."),

    // Authentication errors
    UNAUTHORIZED("AUTH_001", "인증되지 않은 사용자입니다."),
    INVALID_TOKEN("AUTH_002", "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN("AUTH_003", "만료된 토큰입니다."),
    EMPTY_TOKEN("AUTH_004", "토큰이 없습니다.");

    private final String code;
    private final String message;
}
