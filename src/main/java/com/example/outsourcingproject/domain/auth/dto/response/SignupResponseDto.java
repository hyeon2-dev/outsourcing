package com.example.outsourcingproject.domain.auth.dto.response;

import com.example.outsourcingproject.domain.user.enums.UserRole;
import lombok.Getter;

@Getter
public class SignupResponseDto {

    private final Long id;
    private final String name;
    private final String email;
    private final String phone;
    private final String address;
    private final UserRole userRole;

    public SignupResponseDto(Long id, String name, String email, String phone, String address, UserRole userRole) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.userRole = userRole;
    }
}
