package com.example.outsourcingproject.domain.store.dto.response;

import com.example.outsourcingproject.domain.store.entity.Store;
import com.example.outsourcingproject.domain.store.enums.StoreCategory;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class StoreOwnerResponseDto {

    private final Long id;
    private final Long userId;
    private final String name;
    private final String address;
    private final String storePhone;
    private final StoreCategory category;
    private final int minPrice;
    private final boolean closed;
    private final double rating;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public StoreOwnerResponseDto(Long id, Long userId, String name, String address, String storePhone, StoreCategory category, int minPrice, boolean closed, double rating, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.address = address;
        this.storePhone = storePhone;
        this.category = category;
        this.minPrice = minPrice;
        this.closed = closed;
        this.rating = rating;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public static StoreOwnerResponseDto toDto(Store store) {
        return new StoreOwnerResponseDto(
                store.getId(),
                store.getUser().getId(),
                store.getName(),
                store.getAddress(),
                store.getStorePhone(),
                store.getCategory(),
                store.getMinPrice(),
                store.isClosed(),
                store.getRating(),
                store.getCreatedAt(),
                store.getModifiedAt()
        );
    }
}
