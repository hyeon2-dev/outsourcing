package com.example.outsourcingproject.domain.order.dto.response;

import com.example.outsourcingproject.domain.order.entity.Order;
import com.example.outsourcingproject.domain.order.enums.OrderStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class OrderResponseDto {

    private final Long id;
    private final Long userId;
    private final Long storeId;
    private final String storeName;
    private final int totalPrice;
    private final OrderStatus status;
    private final LocalDateTime createdAt;

    public OrderResponseDto(Long id, Long userId, Long storeId, String storeName, int totalPrice, OrderStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.storeId = storeId;
        this.storeName = storeName;
        this.totalPrice = totalPrice;
        this.status = status;
        this.createdAt = createdAt;
    }

    public static OrderResponseDto toDto (Order order) {
        return new OrderResponseDto(
                order.getId(),
                order.getUser().getId(),
                order.getStore().getId(),
                order.getStore().getName(),
                order.getTotalPrice(),
                order.getStatus(),
                order.getCreatedAt()
        );
    }
}
