package com.example.outsourcingproject.domain.user.controller;

import com.example.outsourcingproject.common.annotation.Auth;
import com.example.outsourcingproject.common.dto.AuthUser;
import com.example.outsourcingproject.domain.user.dto.request.ChangePasswordRequestDto;
import com.example.outsourcingproject.domain.user.dto.request.UserUpdateRequestDto;
import com.example.outsourcingproject.domain.user.dto.response.UserResponseDto;
import com.example.outsourcingproject.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 본인 정보 조회
    @GetMapping("/users/my-profile")
    public ResponseEntity<UserResponseDto> getMyProfile (@Auth AuthUser authUser) {
        return ResponseEntity.ok(userService.getMyProfile(authUser));
    }

    // 본인 정보 수정
    @PutMapping("/users/me")
    public ResponseEntity<UserResponseDto> updateMyProfile (
            @Auth AuthUser authUser,
            @RequestBody UserUpdateRequestDto dto
    ) {
        return ResponseEntity.ok(userService.updateMyProfile(authUser, dto));
    }

    // 본인 비밀번호 수정
    @PutMapping("/users/change-password")
    public ResponseEntity<String> changePassword(
            @Auth AuthUser authUser,
            @RequestBody ChangePasswordRequestDto dto
    ) {
        userService.changePassword(authUser, dto);
        return ResponseEntity.ok("비밀번호가 수정되었습니다.");
    }

    // 회원탈퇴
    @DeleteMapping("/users/me")
    public ResponseEntity<String> deleteUser (@Auth AuthUser authUser) {
        userService.deleteUser(authUser);
        return ResponseEntity.ok("회원탈퇴했습니다.");
    }
}
