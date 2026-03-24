package com.example.outsourcingproject.domain.user.entity;

import com.example.outsourcingproject.common.entity.BaseEntity;
import com.example.outsourcingproject.domain.user.enums.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;
    private String phone;
    private String address;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;
    private boolean deleteFlag;


}
