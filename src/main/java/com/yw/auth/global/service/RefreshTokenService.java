package com.yw.auth.global.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RedisTemplate<String, String> redisTemplate;
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 14 * 24 * 60 * 60; // 14일 (초 단위)

    public void saveRefreshToken(String userId, String refreshToken) {
        redisTemplate.opsForValue().set("RT:" + userId, refreshToken, REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.DAYS);
    }

    public String getRefreshToken(String userId) {
        return redisTemplate.opsForValue().get("RT:" + userId);
    }

    public void deleteRefreshToken(String userId) {
        redisTemplate.delete("RT:" + userId);
    }
}
