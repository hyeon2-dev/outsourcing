package com.example.outsourcingproject.domain.store.controller;

import com.example.outsourcingproject.common.annotation.Auth;
import com.example.outsourcingproject.common.dto.AuthUser;
import com.example.outsourcingproject.domain.store.dto.request.CreateStoreRequestDto;
import com.example.outsourcingproject.domain.store.dto.request.UpdateStoreRequestDto;
import com.example.outsourcingproject.domain.store.dto.response.CreateStoreResponseDto;
import com.example.outsourcingproject.domain.store.dto.response.StoreCustomerResponseDto;
import com.example.outsourcingproject.domain.store.dto.response.StoreOwnerResponseDto;
import com.example.outsourcingproject.domain.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    // Owner-------------------------------------------------------------------
    // 가게 생성
    @PostMapping("/stores")
    public ResponseEntity<CreateStoreResponseDto> createStore (
            @Auth AuthUser authUser,
            @RequestBody CreateStoreRequestDto dto
    ) {
        return ResponseEntity.ok(storeService.createStore(authUser, dto));
    }

    // 본인 가게 조회
    @GetMapping("/stores/me")
    public ResponseEntity<Page<StoreOwnerResponseDto>> getAllMyStores(
            @Auth AuthUser authUser,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(storeService.getAllMyStores(authUser, page, size));
    }

    // 본인 가게 단일 조회
    @GetMapping("/stores/{storeId}")
    public ResponseEntity<StoreOwnerResponseDto> getMyStore(
            @Auth AuthUser authUser,
            @PathVariable Long storeId
    ){
        return ResponseEntity.ok(storeService.getMyStore(authUser, storeId));
    }

    // 본인 가게 정보 수정
    @PutMapping("/stores/{storeId}")
    public ResponseEntity<StoreOwnerResponseDto> updateMyStore(
            @Auth AuthUser authUser,
            @PathVariable Long storeId,
            @RequestBody UpdateStoreRequestDto dto
            ) {
        return ResponseEntity.ok(storeService.updateMyStore(authUser, storeId, dto));
    }

    // 가게 폐업
    @DeleteMapping("/stores/{storeId}/close")
    public ResponseEntity<String> softDeleteMyStore (
            @Auth AuthUser authUser,
            @PathVariable Long storeId
    ) {
        storeService.softDeleteMyStore(authUser, storeId);
        return ResponseEntity.ok("가게를 폐업했습니다.");
    }

    // Customer----------------------------------------------------------------
    // 영업중인 가게 전체 조회
    @GetMapping("/stores")
    public ResponseEntity<Page<StoreCustomerResponseDto>> getStores(
            @Auth AuthUser authUser,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(storeService.getStores(authUser, page, size));
    }

    // 가게 단일 조회
    @GetMapping("/stores/{storeId}")
    public ResponseEntity<StoreCustomerResponseDto> getStore (
            @Auth AuthUser authUser,
            @PathVariable Long storeId
    ) {
        return ResponseEntity.ok(storeService.getStore(authUser, storeId));
    }

}
