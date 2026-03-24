package com.example.outsourcingproject.domain.auth.service;

import com.example.outsourcingproject.common.encoder.PasswordEncoder;
import com.example.outsourcingproject.common.exception.BaseException;
import com.example.outsourcingproject.common.exception.ErrorCode;
import com.example.outsourcingproject.config.JwtUtil;
import com.example.outsourcingproject.domain.auth.dto.request.LoginRequestDto;
import com.example.outsourcingproject.domain.auth.dto.response.AuthUser;
import com.example.outsourcingproject.domain.auth.dto.response.TokenResponseDto;
import com.example.outsourcingproject.domain.user.entity.User;
import com.example.outsourcingproject.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRedisService refreshTokenRedisService;

    @Transactional
    public TokenResponseDto login(LoginRequestDto dto) {
        User user = userRepository.findByEmail(dto.getEmail()).orElseThrow(
                () -> new BaseException(ErrorCode.USER_NOT_FOUND, null)
        );

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BaseException(ErrorCode.INVALID_PASSWORD, null);
        }

        String accessToken = jwtUtil.createAccessToken(
                user.getId(),
                user.getEmail(),
                user.getUserRole()
        );

        String refreshToken = jwtUtil.createRefreshToken(user.getId());

        refreshTokenRedisService.saveRefreshToken(
                user.getId(),
                refreshToken,
                jwtUtil.getRefreshTokenTime()
        );

        return new TokenResponseDto(accessToken, refreshToken);
    }

    @Transactional
    public void logout(AuthUser authuser) {
        refreshTokenRedisService.deleteRefreshToken(authuser.getUserId());
    }

    public TokenResponseDto reissueToken(String bearerRefreshToken) {
        String refreshToken = jwtUtil.substringToken(bearerRefreshToken);

        jwtUtil.validateRefreshToken(refreshToken);
        Long userId = jwtUtil.getUserId(refreshToken);

        String savedRefreshToken = refreshTokenRedisService.getRefreshToken(userId);

        if(savedRefreshToken == null) {
            throw new BaseException(ErrorCode.INVALID_TOKEN, null);
        }

        String savedTokenWithoutBearer = jwtUtil.substringToken(savedRefreshToken);

        if(!savedTokenWithoutBearer.equals(refreshToken)) {
            throw new BaseException(ErrorCode.INVALID_TOKEN, null);
        }

        User user = userRepository.findById(userId).orElseThrow(
                () -> new BaseException(ErrorCode.USER_NOT_FOUND, null)
        );

        String newAccessToken = jwtUtil.createAccessToken(
                user.getId(),
                user.getEmail(),
                user.getUserRole()
        );

        return new TokenResponseDto(newAccessToken, savedRefreshToken);
    }
}
