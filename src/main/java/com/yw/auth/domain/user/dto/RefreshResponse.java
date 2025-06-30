package com.yw.auth.domain.user.dto;

import lombok.Getter;

@Getter
public class RefreshResponse {
    public record RefreshTokenResponse(
            String accessToken,
            String refreshToken
    ) {
        public static RefreshTokenResponse from(String accessToken, String refreshToken) {
            return new RefreshTokenResponse(accessToken, refreshToken);
        }
    }
}
