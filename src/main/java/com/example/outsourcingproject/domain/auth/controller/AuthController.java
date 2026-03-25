package com.example.outsourcingproject.domain.auth.controller;

import com.example.outsourcingproject.common.annotation.Auth;
import com.example.outsourcingproject.domain.auth.dto.request.LoginRequestDto;
import com.example.outsourcingproject.domain.auth.dto.request.RefreshTokenRequestDto;
import com.example.outsourcingproject.common.dto.AuthUser;
import com.example.outsourcingproject.domain.auth.dto.request.SignupRequestDto;
import com.example.outsourcingproject.domain.auth.dto.response.SignupResponseDto;
import com.example.outsourcingproject.domain.auth.dto.response.TokenResponseDto;
import com.example.outsourcingproject.domain.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // 회원가입
    @PostMapping("/auth/signup")
    public ResponseEntity<SignupResponseDto> signup (@Valid @RequestBody SignupRequestDto dto) {
        return ResponseEntity.ok(authService.signup(dto));
    }

    // 로그인
    @PostMapping("/auth/login")
    public ResponseEntity<TokenResponseDto> login (
            @RequestBody LoginRequestDto dto
    ) {
        return ResponseEntity.ok(authService.login(dto));
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@Auth AuthUser authuser) {
        authService.logout(authuser);
        return ResponseEntity.ok("로그아웃되었습니다.");
    }

    // refresh token을 이용해 access token 재발급
    @PostMapping("/reissue")
    public ResponseEntity<TokenResponseDto> reissue(@RequestBody RefreshTokenRequestDto dto) {
        return ResponseEntity.ok(authService.reissueToken(dto.getRefreshToken()));
    }
}
