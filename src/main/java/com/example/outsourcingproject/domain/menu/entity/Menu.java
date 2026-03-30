package com.example.outsourcingproject.domain.menu.entity;

import com.example.outsourcingproject.common.entity.BaseEntity;
import com.example.outsourcingproject.common.exception.BaseException;
import com.example.outsourcingproject.common.exception.ErrorCode;
import com.example.outsourcingproject.domain.store.entity.Store;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "Menus")
public class Menu extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    private String name;
    private int price;
    private boolean deleteFlag;

    public Menu(Store store, String name, int price) {
        this.store = store;
        this.name = name;
        this.price = price;
    }

    public void update(String name, Integer price) {
        if(name != null) {this.name = name;}
        if(price != null) {this.price = price;}
    }

    public void deleted() {
        if(this.deleteFlag) {
            throw new BaseException(ErrorCode.MENU_ALREADY_DELETE, null);
        }
        this. deleteFlag = true;
    }
}
