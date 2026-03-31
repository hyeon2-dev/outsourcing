package com.example.outsourcingproject.domain.store.entity;

import com.example.outsourcingproject.common.entity.BaseEntity;
import com.example.outsourcingproject.common.exception.BaseException;
import com.example.outsourcingproject.common.exception.ErrorCode;
import com.example.outsourcingproject.domain.store.enums.StoreCategory;
import com.example.outsourcingproject.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "stores")
public class Store extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", unique = false)
    private User user;

    private String name;
    private String address;
    private String storePhone;
    private int minPrice;

    @Enumerated(EnumType.STRING)
    private StoreCategory category;

    private boolean closed;
    private double rating;

    public Store(User user, String name, String address, String storePhone, int minPrice, StoreCategory category) {
        this.user = user;
        this.name = name;
        this.address = address;
        this.storePhone = storePhone;
        this.minPrice = minPrice;
        this.category = category;
    }

    public void update(String name, String address, String storePhone, StoreCategory category, Integer minPrice) {
        if(name != null) {this.name = name;}
        if(address != null) {this.address = address;}
        if(storePhone != null) {this.storePhone = storePhone;}
        if(category != null) {this.category = category;}
        if(minPrice != null) {this.minPrice = minPrice;}
    }

    public void closeStore() {
        if(this.closed) {
            throw new BaseException(ErrorCode.STORE_ALREADY_DELETE, null);
        }
        this.closed = true;
    }
}
