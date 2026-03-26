package com.example.outsourcingproject.domain.user.dto.request;

import lombok.Getter;

@Getter
public class ChangePasswordRequestDto {

    private String oldPassword;
    private String newPassword;
    private String newPasswordCheck;

}
