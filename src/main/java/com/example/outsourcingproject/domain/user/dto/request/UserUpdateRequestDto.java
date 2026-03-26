package com.example.outsourcingproject.domain.user.dto.request;

import lombok.Getter;

@Getter
public class UserUpdateRequestDto {

    private String name;
    private String email;
    private String phone;
    private String address;

}
