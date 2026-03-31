package com.example.outsourcingproject.domain.order.enums;

public enum OrderStatus {
    PENDING,    // 주문 요청
    ACCEPTED,   // 주문 수락
    REJECTED,   // 주문 거절
    COOKING,    // 조리중
    DELIVERING, // 배달중
    COMPLETED,  // 완료
    CANCELED    // 사용자 취소
}
