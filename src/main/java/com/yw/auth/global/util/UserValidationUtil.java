package com.yw.auth.global.util;

import com.yw.auth.global.exception.UnauthorizedException;
import com.yw.auth.global.response.ErrorCode;

public class UserValidationUtil {
    public static void validateUserId(String userId) {
        if (userId == null || userId.isEmpty()) {
            throw new UnauthorizedException(ErrorCode.EMPTY_TOKEN.getMessage());
        }
    }
}
