package com.example.outsourcingproject.domain.auth.controller;

import com.example.outsourcingproject.common.annotation.Auth;
import com.example.outsourcingproject.domain.auth.dto.request.LoginRequestDto;
import com.example.outsourcingproject.domain.auth.dto.request.RefreshTokenRequestDto;
import com.example.outsourcingproject.domain.auth.dto.response.AuthUser;
import com.example.outsourcingproject.domain.auth.dto.response.TokenResponseDto;
import com.example.outsourcingproject.domain.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // 회원가입


    // 로그인
    @PostMapping("/auth/login")
    public ResponseEntity<TokenResponseDto> login (
            @RequestBody LoginRequestDto dto
    ) {
        return ResponseEntity.ok(authService.login(dto));
    }

    // 로그아웃
    @PostMapping("/logout")
    public void logout(@Auth AuthUser authuser) {
        authService.logout(authuser);
    }

    // refresh token 재발급
    @PostMapping("/reissue")
    public ResponseEntity<TokenResponseDto> reissue(@RequestBody RefreshTokenRequestDto dto) {
        return ResponseEntity.ok(authService.reissueToken(dto.getRefreshToken()));
    }
}
