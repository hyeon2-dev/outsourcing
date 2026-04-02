package com.example.outsourcingproject.domain.order.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class OrderMenuRequestDto {

    @NotNull
    private final Long menuId;
    @Min(1)
    private final int quantity;

    public OrderMenuRequestDto(Long menuId, int quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }
}
