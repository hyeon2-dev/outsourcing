package com.example.outsourcingproject.domain.menu.dto.response;

import com.example.outsourcingproject.domain.menu.entity.Menu;
import lombok.Getter;

@Getter
public class MenuResponseDto {

    private final Long id;
    private final Long storeId;
    private final String name;
    private final int price;

    public MenuResponseDto(Long id, Long storeId, String name, int price) {
        this.id = id;
        this.storeId = storeId;
        this.name = name;
        this.price = price;
    }

    public static MenuResponseDto toDto(Menu menu) {
        return new MenuResponseDto(
                menu.getId(),
                menu.getStore().getId(),
                menu.getName(),
                menu.getPrice()
        );
    }
}
