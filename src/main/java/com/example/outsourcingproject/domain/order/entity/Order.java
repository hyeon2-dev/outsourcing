package com.example.outsourcingproject.domain.order.entity;

import com.example.outsourcingproject.common.entity.BaseEntity;
import com.example.outsourcingproject.domain.order.enums.OrderStatus;
import com.example.outsourcingproject.domain.store.entity.Store;
import com.example.outsourcingproject.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "orders")
public class Order extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(nullable = false)
    private int totalPrice;

    @Column(length = 255)
    private String request;

    @Column(length = 255)
    private String rejectReason;

    public Order(User user, Store store, int totalPrice, String request, OrderStatus status) {
        this.user = user;
        this.store = store;
        this.totalPrice = totalPrice;
        this.request = request;
        this.status = OrderStatus.PENDING;
    }
}
