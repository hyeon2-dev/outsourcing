package com.example.outsourcingproject.config;

import com.example.outsourcingproject.common.exception.BaseException;
import com.example.outsourcingproject.common.exception.ErrorCode;
import com.example.outsourcingproject.domain.user.enums.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j(topic = "JwtUtil")
@Component
public class JwtUtil {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final long ACCESS_TOKEN_TIME = 60 * 60 * 1000L; // 60분
    // refresh token
    private static final long REFRESH_TOKEN_TIME = 7 * 24  * 60 * 60 * 1000L; // 7일

    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // Access Token 생성
    public String createAccessToken(Long userId, String email, UserRole userRole) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(String.valueOf(userId))
                        .claim("email", email)
                        .claim("userRole", userRole.name())
                        .claim("tokenType", "ACCESS")
                        .setExpiration(new Date(date.getTime() + ACCESS_TOKEN_TIME))
                        .setIssuedAt(date) // 발급일
                        .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                        .compact();
    }

    // Refresh Token 생성
    public String createRefreshToken(Long userId) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(String.valueOf(userId))
                        .claim("tokenType", "REFRESH")
                        .setIssuedAt(date)
                        .setExpiration(new Date(date.getTime() + REFRESH_TOKEN_TIME))
                        .signWith(key, signatureAlgorithm)
                        .compact();
    }

    // Bearer 제거
    public String substringToken(String tokenValue) {
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(7);
        }
        throw new BaseException(ErrorCode.NOT_FOUND_TOKEN, null);
    }

    // Claims 추출
    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // userId 추출
    public Long getUserId(String token) {
        return Long.parseLong(extractClaims(token).getSubject());
    }

    // tokenType 추출
    public String getTokenType(String token) {
        return extractClaims(token).get("tokenType", String.class);
    }

    // Refresh Token인지 검증
    public void validateRefreshToken(String refreshToken) {
        String tokenType = getTokenType(refreshToken);
        if (!"REFRESH".equals(tokenType)) {
            throw new BaseException(ErrorCode.INVALID_TOKEN, null);
        }
    }

    // Access Token인지 검증
    public void validateAccessToken(String accessToken) {
        String tokenType = getTokenType(accessToken);
        if (!"ACCESS".equals(tokenType)) {
            throw new BaseException(ErrorCode.INVALID_TOKEN, null);
        }
    }

    // Refresh Token 만료 시간 추출
    public long getRefreshTokenTime() {
        return REFRESH_TOKEN_TIME;
    }
}