package com.example.outsourcingproject.domain.order.service;

import com.example.outsourcingproject.common.dto.AuthUser;
import com.example.outsourcingproject.common.exception.BaseException;
import com.example.outsourcingproject.common.exception.ErrorCode;
import com.example.outsourcingproject.domain.menu.entity.Menu;
import com.example.outsourcingproject.domain.menu.repository.MenuRepository;
import com.example.outsourcingproject.domain.order.dto.request.OrderCreateRequestDto;
import com.example.outsourcingproject.domain.order.dto.request.OrderMenuRequestDto;
import com.example.outsourcingproject.domain.order.dto.response.OrderResponseDto;
import com.example.outsourcingproject.domain.order.entity.Order;
import com.example.outsourcingproject.domain.order.repository.OrderRepository;
import com.example.outsourcingproject.domain.store.entity.Store;
import com.example.outsourcingproject.domain.store.repository.StoreRepository;
import com.example.outsourcingproject.domain.user.entity.User;
import com.example.outsourcingproject.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;

    // 주문요청(유저)
    @Transactional
    public void createOrder(AuthUser authUser, Long storeId, OrderCreateRequestDto dto) {
        User user = findUserByIdOrThrow(authUser.getUserId());
        Store store = findStoreByIdOrThrow(storeId);
        int totalPrice = calculateTotalPrice(store, dto.getItems());

        // 가게 최소 주문 금액 검증
        if(store.getMinPrice() > totalPrice) {
            throw new BaseException(ErrorCode.MIN_ORDER_AMOUNT_NOT_MET, null);
        }

        Order order = new Order(user, store, totalPrice, dto.getRequest());
        orderRepository.save(order);
    }

    // 본인 주문 목록 조회(유저)
    @Transactional(readOnly = true)
    public Page<OrderResponseDto> getMyOrders(AuthUser authUser, int page, int size) {
        findActiveUserByIdOrThrow(authUser.getUserId());

        int adjustPage = (page > 0) ? page - 1 : 0;

        Pageable pageable = PageRequest.of(adjustPage, size, Sort.by("createdAt").descending());

        return orderRepository.findAllByUserId(authUser.getUserId(), pageable)
                .map(OrderResponseDto::toDto);

    }

    // 본인 주문 단건 조회(유저)
    @Transactional(readOnly = true)
    public OrderResponseDto getMyOrderById(AuthUser authUser, Long orderId) {
        Order order = orderRepository.findByIdAndUserId(orderId, authUser.getUserId()).orElseThrow(
                () -> new BaseException(ErrorCode.ORDER_NOT_FOUND, null)
        );

        return OrderResponseDto.toDto(order);
    }





    // userId로 사용자를 조회하고, 없으면 USER_NOT_FOUND 예외를 발생시킨다.
    private User findUserByIdOrThrow(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new BaseException(ErrorCode.USER_NOT_FOUND, null)
        );
    }

    // storeId로 가게를 조회하고, 해당 가게가 아니면 STORE_NOT_FOUND 예외를 발생시킨다.
    private Store findStoreByIdOrThrow(Long storeId) {
        return storeRepository.findStoreById(storeId).orElseThrow(
                () -> new BaseException(ErrorCode.STORE_NOT_FOUND, null)
        );
    }

    // menuId로 가게를 조회하고, 해당 메뉴가 아니면 MENU_NOT_FOUND 예외를 발생시킨다.
    private Menu findMenuByIdOrThrow(Long menuId) {
        return menuRepository.findById(menuId).orElseThrow(
                () -> new BaseException(ErrorCode.MENU_NOT_FOUND, null)
        );
    }

    // totalPrice 계산
    private int calculateTotalPrice(Store store, List<OrderMenuRequestDto> items) {
        int totalPrice = 0;
        Set<Long> menuIds = new HashSet<>();
        for(OrderMenuRequestDto item : items) {

            // 메뉴 수량 음수가 안되도록 유지
            if(item.getQuantity() <= 0) {
                throw new BaseException(ErrorCode.INVALID_ORDER_QUANTITY, null);
            }
            if(!menuIds.add(item.getMenuId())) {
                throw new BaseException(ErrorCode.DUPLICATE_MENU, null);
            }

            Menu menu = findMenuByIdOrThrow(item.getMenuId());

            // 해당 가게에 속한 메뉴가 맞는지 검증
            if(!menu.getStore().getId().equals(store.getId())){
                throw new BaseException(ErrorCode.INVALID_MENU_STORE, null);
            }

            totalPrice += menu.getPrice() * item.getQuantity();
        }

        return totalPrice;
    }

    // 활성화 유저 조회
    private User findActiveUserByIdOrThrow(Long userId) {
        User user = findUserByIdOrThrow(userId);

        // 회원 탈퇴 유무
        if (user.isDeleteFlag()) {
            throw new BaseException(ErrorCode.USER_ALREADY_DELETE, null);
        }

        return user;
    }

}
