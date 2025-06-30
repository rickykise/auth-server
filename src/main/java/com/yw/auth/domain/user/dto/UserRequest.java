package com.yw.auth.domain.user.dto;

public class UserRequest {

    public record UserLoginRequest(
            String userId,
            String userPassword
    ) {
    }
}
