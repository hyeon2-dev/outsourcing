package com.example.outsourcingproject.domain.user.service;

import com.example.outsourcingproject.common.dto.AuthUser;
import com.example.outsourcingproject.common.encoder.PasswordEncoder;
import com.example.outsourcingproject.common.exception.BaseException;
import com.example.outsourcingproject.common.exception.ErrorCode;
import com.example.outsourcingproject.domain.user.dto.request.ChangePasswordRequestDto;
import com.example.outsourcingproject.domain.user.dto.request.UserUpdateRequestDto;
import com.example.outsourcingproject.domain.user.dto.response.UserResponseDto;
import com.example.outsourcingproject.domain.user.entity.User;
import com.example.outsourcingproject.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 본인 정보 조회
    @Transactional(readOnly = true)
    public UserResponseDto getMyProfile(AuthUser authUser) {
        User user = findUserByIdOrThrow(authUser.getUserId());

        return new UserResponseDto(
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getAddress()
        );
    }

    // 본인 정보 수정
    @Transactional
    public UserResponseDto updateMyProfile(AuthUser authUser, UserUpdateRequestDto dto) {
        User user = findUserByIdOrThrow(authUser.getUserId());

        user.updateProfile(dto.getName(), dto.getEmail(), dto.getPhone(), dto.getAddress());

        return new UserResponseDto(
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getAddress()
        );
    }

    // 본인 비밀번호 수정
    @Transactional
    public void changePassword(AuthUser authUser, ChangePasswordRequestDto dto) {
        User user = findUserByIdOrThrow(authUser.getUserId());

        // 비밀번호 수정시, 본인 확인 (비밀번호 일치 여부 확인)
        if(!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new BaseException(ErrorCode.INVALID_PASSWORD, null);
        }

        // 현재 비밀번호와 새 비밀번호가 같은지 확인
        if(dto.getOldPassword().equals(dto.getNewPassword())) {
            throw new BaseException(ErrorCode.SAME_AS_OLD_PASSWORD, null);
        }

        // 새 비밀번호와 새 비밀번호 확인 번호가 일치하는지 확인
        if(!dto.getNewPassword().equals(dto.getNewPasswordCheck())) {
            throw new BaseException(ErrorCode.PASSWORD_CONFIRMATION_MISMATCH, null);
        }

        String encodedNewPassword = passwordEncoder.encode(dto.getNewPassword());
        user.changePassword(encodedNewPassword);

    }

    @Transactional
    public void deleteUser(AuthUser authUser) {
        User user = findUserByIdOrThrow(authUser.getUserId());

        user.delete();
    }

    // userId로 사용자를 조회하고, 없으면 USER_NOT_FOUND 예외를 발생시킨다.
    private User findUserByIdOrThrow(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new BaseException(ErrorCode.USER_NOT_FOUND, null)
        );
    }

}
