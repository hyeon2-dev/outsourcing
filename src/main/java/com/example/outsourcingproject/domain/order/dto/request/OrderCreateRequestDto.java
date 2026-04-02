package com.example.outsourcingproject.domain.order.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderCreateRequestDto {

    @NotEmpty
    private List<OrderMenuRequestDto> items;

    private String request;

}
