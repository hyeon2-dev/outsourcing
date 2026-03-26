package com.example.outsourcingproject.domain.user.dto.response;

import lombok.Getter;

@Getter
public class UserResponseDto {

    private final String name;
    private final String email;
    private final String phone;
    private final String address;

    public UserResponseDto(String name, String email, String phone, String address) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }
}
