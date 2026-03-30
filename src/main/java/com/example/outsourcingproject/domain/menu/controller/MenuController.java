package com.example.outsourcingproject.domain.menu.controller;

import com.example.outsourcingproject.common.annotation.Auth;
import com.example.outsourcingproject.common.dto.AuthUser;
import com.example.outsourcingproject.domain.menu.dto.request.CreateMenuRequestDto;
import com.example.outsourcingproject.domain.menu.dto.request.UpdateMenuRequestDto;
import com.example.outsourcingproject.domain.menu.dto.response.MenuResponseDto;
import com.example.outsourcingproject.domain.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    // 메뉴 생성(오너)
    @PostMapping("/owner/stores/{storeId}/menus")
    public ResponseEntity<MenuResponseDto> createMenu (
            @Auth AuthUser authUser,
            @RequestBody CreateMenuRequestDto dto,
            @PathVariable Long storeId
    ) {
        return ResponseEntity.ok(menuService.createMenu(authUser, dto, storeId));
    }

    // 메뉴 전체 조회
    @GetMapping("/stores/{storeId}/menus")
    public ResponseEntity<Page<MenuResponseDto>> getAllMenus(
            @Auth AuthUser authUser,
            @PathVariable Long storeId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(menuService.getAllMenus(authUser, storeId, page, size));
    }

    // 메뉴 단건 조회
    @GetMapping("/stores/{storeId}/menus/{menuId}")
    public ResponseEntity<MenuResponseDto> getMenu (
            @Auth AuthUser authUser,
            @PathVariable Long storeId,
            @PathVariable Long menuId
    ) {
        return ResponseEntity.ok(menuService.getMenu(authUser, storeId, menuId));
    }

    // 메뉴 수정(오너)
    @PutMapping("/owner/stores/{storeId}/menus/{menuId}")
    public ResponseEntity<MenuResponseDto> updateMenu (
            @Auth AuthUser authUser,
            @PathVariable Long storeId,
            @PathVariable Long menuId,
            @RequestBody UpdateMenuRequestDto dto
    ) {
        return ResponseEntity.ok(menuService.updateMenu(authUser, storeId, menuId, dto));
    }

    // 메뉴 삭제(오너)
    @DeleteMapping("/owner/stores/{storeId}/menus/{menuId}")
    public ResponseEntity<String> deleteMenu (
            @Auth AuthUser authUser,
            @PathVariable Long storeId,
            @PathVariable Long menuId
    ) {
        menuService.deleteMenu(authUser, storeId, menuId);
        return ResponseEntity.ok("메뉴가 삭제되었습니다");
    }
}
