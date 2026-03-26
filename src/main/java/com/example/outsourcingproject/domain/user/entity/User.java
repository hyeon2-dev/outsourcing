package com.example.outsourcingproject.domain.user.entity;

import com.example.outsourcingproject.common.entity.BaseEntity;
import com.example.outsourcingproject.common.exception.BaseException;
import com.example.outsourcingproject.common.exception.ErrorCode;
import com.example.outsourcingproject.domain.user.enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
    private String password;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    private boolean deleteFlag;

    public User(String name, String email, String password, String phone, String address, UserRole userRole) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.userRole = userRole;
    }

    // 본인 정보 수정
    public void updateProfile(String name, String email, String phone, String address) {
        if(name != null && !name.isBlank()) {
            this.name = name;
        }
        if(email != null && !email.isBlank()) {
            this.email = email;
        }
        if(phone != null && !phone.isBlank()) {
            this.phone = phone;
        }
        if(address != null && !address.isBlank()) {
            this.address = address;
        }
    }

    public void changePassword(String encodedNewPassword) {
        this.password = password;
    }

    public void delete() {
        if (this.deleteFlag) {
            throw new BaseException(ErrorCode.USER_ALREADY_DELETE, null);
        }
        this.deleteFlag = true;
    }
}
