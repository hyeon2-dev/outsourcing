package com.example.outsourcingproject.domain.auth.service;

import com.example.outsourcingproject.common.encoder.PasswordEncoder;
import com.example.outsourcingproject.common.exception.BaseException;
import com.example.outsourcingproject.common.exception.ErrorCode;
import com.example.outsourcingproject.config.JwtUtil;
import com.example.outsourcingproject.domain.auth.dto.request.LoginRequestDto;
import com.example.outsourcingproject.common.dto.AuthUser;
import com.example.outsourcingproject.domain.auth.dto.request.SignupRequestDto;
import com.example.outsourcingproject.domain.auth.dto.response.SignupResponseDto;
import com.example.outsourcingproject.domain.auth.dto.response.TokenResponseDto;
import com.example.outsourcingproject.domain.user.entity.User;
import com.example.outsourcingproject.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRedisService refreshTokenRedisService;

    // 회원가입
    public SignupResponseDto signup(SignupRequestDto dto) {
        // 중복 이메일이 있는지 검증
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new BaseException(ErrorCode.DUPLICATE_EMAIL, null);
        }

        // 비밀번호와 비밀번호 확인 값이 일치하는지 검증
        if (!dto.getPassword().equals(dto.getCheckPassword())) {
            throw new BaseException(ErrorCode.PASSWORD_CONFIRMATION_MISMATCH, null);
        }

        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        User user = new User(dto.getName(), dto.getEmail(), encodedPassword, dto.getPhone(), dto.getAddress(), dto.getUserRole());

        userRepository.save(user);

        return new SignupResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getAddress(),
                user.getUserRole()
        );
    }

    // 로그인
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

    // 로그아웃
    @Transactional
    public void logout(AuthUser authuser) {
        refreshTokenRedisService.deleteRefreshToken(authuser.getUserId());
    }

    // refresh token을 이용해 access token 재발급
    @Transactional
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
