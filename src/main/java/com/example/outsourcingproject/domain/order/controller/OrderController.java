package com.example.outsourcingproject.domain.order.controller;

import com.example.outsourcingproject.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // 주문요청(유저)
    // 본인 주문 목록 조회(유저)
    // 본인 주문 단건 조회(유저)
    // 주문 상태 변경(오너)
    // 가게 주문 목록 조회(오너)
    //가게 주문 단거 조회(오너)

}
