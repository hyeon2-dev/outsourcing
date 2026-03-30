package com.example.outsourcingproject.domain.menu.service;

import com.example.outsourcingproject.common.dto.AuthUser;
import com.example.outsourcingproject.common.exception.BaseException;
import com.example.outsourcingproject.common.exception.ErrorCode;
import com.example.outsourcingproject.domain.menu.dto.request.CreateMenuRequestDto;
import com.example.outsourcingproject.domain.menu.dto.request.UpdateMenuRequestDto;
import com.example.outsourcingproject.domain.menu.dto.response.MenuResponseDto;
import com.example.outsourcingproject.domain.menu.entity.Menu;
import com.example.outsourcingproject.domain.menu.repository.MenuRepository;
import com.example.outsourcingproject.domain.store.entity.Store;
import com.example.outsourcingproject.domain.store.repository.StoreRepository;
import com.example.outsourcingproject.domain.user.entity.User;
import com.example.outsourcingproject.domain.user.enums.UserRole;
import com.example.outsourcingproject.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public MenuResponseDto createMenu(AuthUser authUser, CreateMenuRequestDto dto, Long storeId) {
        User user = findUserByIdOrThrow(authUser.getUserId());
        validateStoreOwner(user);

        Store store = findStoreByIdOrThrow(storeId);

        Menu menu = new Menu(store, dto.getName(), dto.getPrice());
        menuRepository.save(menu);

        return MenuResponseDto.toDto(menu);
    }

    // 메뉴 전체 조회
    @Transactional(readOnly = true)
    public Page<MenuResponseDto> getAllMenus(AuthUser authUser, Long storeId, int page, int size) {
        findUserByIdOrThrow(authUser.getUserId());
        Store store = findStoreByIdOrThrow(storeId);
        int adjustPage = (page > 0) ? page - 1 : 0;

        Pageable pageable = PageRequest.of(adjustPage, size, Sort.by("createdAt").descending());

        return menuRepository.findAllByStoreIdAndDeleteFlagFalse(storeId, pageable)
                .map(MenuResponseDto::toDto);
    }

    // 메뉴 단건 조회
    @Transactional(readOnly = true)
    public MenuResponseDto getMenu(AuthUser authUser, Long storeId, Long menuId) {
        findUserByIdOrThrow(authUser.getUserId());
        Store store = findStoreByIdOrThrow(storeId);

        Menu menu = findMenuByIdOrThrow(menuId);

        return MenuResponseDto.toDto(menu);
    }

    // 메뉴 수정(오너)
    @Transactional
    public MenuResponseDto updateMenu(AuthUser authUser, Long storeId, Long menuId, UpdateMenuRequestDto dto) {
        User user = findUserByIdOrThrow(authUser.getUserId());
        validateStoreOwner(user);

        findStoreByIdOrThrow(storeId);
        Menu menu = findMenuByIdOrThrow(menuId);

        menu.update(dto.getName(), dto.getPrice());

        return MenuResponseDto.toDto(menu);
    }

    // 메뉴 삭제(오너)
    @Transactional
    public void deleteMenu(AuthUser authUser, Long storeId, Long menuId) {
        User user = findUserByIdOrThrow(authUser.getUserId());
        validateStoreOwner(user);

        findStoreByIdOrThrow(storeId);
        Menu menu = findMenuByIdOrThrow(menuId);

        menu.deleted();
    }

    // userId로 사용자를 조회하고, 없으면 USER_NOT_FOUND 예외를 발생시킨다.
    private User findUserByIdOrThrow(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new BaseException(ErrorCode.USER_NOT_FOUND, null)
        );
    }

    // owner 인지 확인하는 메서드
    private void validateStoreOwner(User user) {
        if (user.getUserRole() != UserRole.OWNER) {
            throw new BaseException(ErrorCode.FORBIDDEN_STORE_CREATION, null);
        }
    }

    // storeId로 가게를 조회하고, 해당 가게가 아니면 STORE_NOT_FOUND 예외를 발생시킨다.
    private Store findStoreByIdOrThrow(Long storeId) {
        return storeRepository.findStoreById(storeId).orElseThrow(
                () -> new BaseException(ErrorCode.STORE_NOT_FOUND, null)
        );
    }

    // menuId로 메뉴를 조회하고, 해당 메뉴가 아니면 MENU_NOT_FOUND 예외를 발생시킨다.
    private Menu findMenuByIdOrThrow(Long menuId) {
        return menuRepository.findById(menuId).orElseThrow(
                () -> new BaseException(ErrorCode.MENU_NOT_FOUND, null)
        );
    }



}
