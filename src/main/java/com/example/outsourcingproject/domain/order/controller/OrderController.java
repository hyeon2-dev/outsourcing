package com.example.outsourcingproject.domain.order.controller;

import com.example.outsourcingproject.common.annotation.Auth;
import com.example.outsourcingproject.common.dto.AuthUser;
import com.example.outsourcingproject.domain.order.dto.request.OrderCreateRequestDto;
import com.example.outsourcingproject.domain.order.dto.response.OrderResponseDto;
import com.example.outsourcingproject.domain.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // 주문요청(유저)
    @PostMapping("/stores/{storeId}/orders")
    public ResponseEntity<String> createOrder(
            @Auth AuthUser authUser,
            @PathVariable Long storeId,
            @Valid @RequestBody OrderCreateRequestDto dto
    ) {
        orderService.createOrder(authUser, storeId, dto);
        return ResponseEntity.ok("주문이 완료되었습니다.");
    }

    // 본인 주문 목록 조회(유저)
    @GetMapping("/users/me/orders")
    public ResponseEntity<Page<OrderResponseDto>> getMyOrders(
            @Auth AuthUser authUser,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(orderService.getMyOrders(authUser, page, size));
    }

    // 본인 주문 단건 조회(유저)
    @GetMapping("/users/me/order/{orderId}")
    public ResponseEntity<OrderResponseDto> getMyOrderById (
            @Auth AuthUser authUser,
            @PathVariable Long orderId
    ) {
        return ResponseEntity.ok(orderService.getMyOrderById(authUser, orderId));
    }

    // 주문 상태 변경(오너)
    // 가게 주문 목록 조회(오너)
    //가게 주문 단거 조회(오너)

}
