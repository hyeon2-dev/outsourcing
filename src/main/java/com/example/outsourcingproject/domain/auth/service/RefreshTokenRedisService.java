package com.example.outsourcingproject.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RefreshTokenRedisService {

    private final StringRedisTemplate stringRedisTemplate;
    private static final String REFRESH_TOKEN_PREFIX = "refresh:";

    public void saveRefreshToken(Long userId, String refreshToken, long expirationMillis) {
        String key = REFRESH_TOKEN_PREFIX + userId;

        stringRedisTemplate.opsForValue().set(
                key,
                refreshToken,
                Duration.ofMillis(expirationMillis)
        );
    }

    public String getRefreshToken(Long userId) {
        return stringRedisTemplate.opsForValue().get(REFRESH_TOKEN_PREFIX + userId);
    }

    public void deleteRefreshToken(Long userId) {
        stringRedisTemplate.delete(REFRESH_TOKEN_PREFIX + userId);
    }

    public boolean existRefreshToken(Long userId) {
        Boolean result = stringRedisTemplate.hasKey(REFRESH_TOKEN_PREFIX + userId);
        return Boolean.TRUE.equals(result);
    }
}
