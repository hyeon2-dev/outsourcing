package com.example.outsourcingproject.domain.store.dto.response;

import com.example.outsourcingproject.domain.store.entity.Store;
import com.example.outsourcingproject.domain.store.enums.StoreCategory;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CreateStoreResponseDto {

    private final Long id;
    private final Long userId;
    private final String name;
    private final String address;
    private final String storePhone;
    private final StoreCategory category;
    private final int minPrice;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public CreateStoreResponseDto(Long id, Long userId, String name, String address, String storePhone, StoreCategory category, int minPrice, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.address = address;
        this.storePhone = storePhone;
        this.category = category;
        this.minPrice = minPrice;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public static CreateStoreResponseDto toDto(Store store) {
        return new CreateStoreResponseDto(
                store.getId(),
                store.getUser().getId(),
                store.getName(),
                store.getAddress(),
                store.getStorePhone(),
                store.getCategory(),
                store.getMinPrice(),
                store.getCreatedAt(),
                store.getModifiedAt()
        );
    }
}
