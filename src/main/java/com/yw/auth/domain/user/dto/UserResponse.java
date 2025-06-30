package com.yw.auth.domain.user.dto;

import lombok.Getter;

@Getter
public class UserResponse {
    public record UserLoginResponse(
            String userId,
            String userName,
            String accessToken
    ) {
        public static UserLoginResponse from(String userId, String userName, String accessToken) {
            return new UserLoginResponse(userId, userName, accessToken);
        }
    }
}

