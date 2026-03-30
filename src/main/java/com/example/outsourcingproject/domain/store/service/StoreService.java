package com.example.outsourcingproject.domain.store.service;

import com.example.outsourcingproject.common.dto.AuthUser;
import com.example.outsourcingproject.common.exception.BaseException;
import com.example.outsourcingproject.common.exception.ErrorCode;
import com.example.outsourcingproject.domain.store.dto.request.CreateStoreRequestDto;
import com.example.outsourcingproject.domain.store.dto.request.UpdateStoreRequestDto;
import com.example.outsourcingproject.domain.store.dto.response.CreateStoreResponseDto;
import com.example.outsourcingproject.domain.store.dto.response.StoreCustomerResponseDto;
import com.example.outsourcingproject.domain.store.dto.response.StoreOwnerResponseDto;
import com.example.outsourcingproject.domain.store.entity.Store;
import com.example.outsourcingproject.domain.store.repository.StoreRepository;
import com.example.outsourcingproject.domain.user.entity.User;
import com.example.outsourcingproject.domain.user.enums.UserRole;
import com.example.outsourcingproject.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    // Owner-------------------------------------------------------------------

    // 가게 생성
    @Transactional
    public CreateStoreResponseDto createStore(AuthUser authUser, CreateStoreRequestDto dto) {
        User user = findUserByIdOrThrow(authUser.getUserId());
        validateStoreOwner(user);
        validateStoreLimit(user);

        Store store = new Store(user, dto.getName(), dto.getAddress(), dto.getStorePhone(), dto.getMinPrice(), dto.getCategory());

        Store savedStore = storeRepository.save(store);

        return CreateStoreResponseDto.toDto(savedStore);
    }

    // 본인 가게 조회
    @Transactional(readOnly = true)
    public Page<StoreOwnerResponseDto> getAllMyStores(AuthUser authUser, int page, int size) {
        int adjustPage = (page > 0) ? page - 1 : 0;

        Pageable pageable = PageRequest.of(adjustPage, size, Sort.by("createdAt").descending());

        return storeRepository.findAllByUserId(authUser.getUserId(), pageable)
                .map(StoreOwnerResponseDto::toDto);
    }

    // 본인 가게 단일 조회
    @Transactional(readOnly = true)
    public StoreOwnerResponseDto getMyStore(AuthUser authUser, Long storeId) {
        Store store = findStoreByIdOrThrow(storeId);
        validateStoreOwner(authUser, store);

        return StoreOwnerResponseDto.toDto(store);
    }

    // 본인 가게 정보 수정
    @Transactional
    public StoreOwnerResponseDto updateMyStore(AuthUser authUser, Long storeId, UpdateStoreRequestDto dto) {
        Store store = findStoreByIdOrThrow(storeId);
        validateStoreOwner(authUser, store);

        store.update(dto.getName(), dto.getAddress(), dto.getStorePhone(), dto.getCategory(), dto.getMinPrice());

        return StoreOwnerResponseDto.toDto(store);
    }

    // 가게 폐업
    @Transactional
    public void softDeleteMyStore(AuthUser authUser, Long storeId) {
        Store store = findStoreByIdOrThrow(storeId);
        validateStoreOwner(authUser, store);
        store.closeStore();
    }

    // Customer----------------------------------------------------------------
    // 가게 다건 조회
    @Transactional(readOnly = true)
    public Page<StoreCustomerResponseDto> getStores(AuthUser authUser, int page, int size) {
        findActiveUserByIdOrThrow(authUser.getUserId());

        int adjustPage = (page > 0) ? page - 1 : 0;

        Pageable pageable = PageRequest.of(adjustPage, size);

        return storeRepository.findAllByClosedFalse(pageable)
                .map(StoreCustomerResponseDto::toDto);
    }

    // 가게 단일 조회
    @Transactional(readOnly = true)
    public StoreCustomerResponseDto getStore(AuthUser authUser, Long storeId) {
        User user = findActiveUserByIdOrThrow(authUser.getUserId());

        Store store = findStoreByIdOrThrow(storeId);

        return StoreCustomerResponseDto.toDto(store);
    }



    // userId로 사용자를 조회하고, 없으면 USER_NOT_FOUND 예외를 발생시킨다.
    private User findUserByIdOrThrow(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new BaseException(ErrorCode.USER_NOT_FOUND, null)
        );
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

    // owner 인지 확인하는 메서드
    private void validateStoreOwner(User user){
        if (user.getUserRole() != UserRole.OWNER) {
            throw new BaseException(ErrorCode.FORBIDDEN_STORE_CREATION, null);
        }
    }

    // owner가 가게를 최대 3개까지만 생성할 수 있게 제한하는 메서드
    private void validateStoreLimit(User user) {
        long activeStoreCount = storeRepository.countByUserIdAndClosedFalse(user.getId());

        if (activeStoreCount >= 3) {
            throw new BaseException(ErrorCode.STORE_LIMIT_EXCEEDED, null);
        }
    }

    // storeId로 가게를 조회하고, 해당 가게가 아니면 STORE_NOT_FOUND 예외를 발생시킨다.
    private Store findStoreByIdOrThrow(Long storeId) {
        return storeRepository.findStoreById(storeId).orElseThrow(
                () -> new BaseException(ErrorCode.STORE_NOT_FOUND, null)
        );
    }

    // 본인 가게가 맞는지 확인하는 메서드
    private void validateStoreOwner(AuthUser authUser, Store store) {
        if (!authUser.getUserId().equals(store.getUser().getId())) {
            throw new BaseException(ErrorCode.FORBIDDEN_STORE, null);
        }
    }



}
