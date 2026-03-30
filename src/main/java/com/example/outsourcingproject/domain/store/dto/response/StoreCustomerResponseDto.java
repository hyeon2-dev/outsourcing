package com.example.outsourcingproject.domain.store.dto.response;

import com.example.outsourcingproject.domain.store.entity.Store;
import com.example.outsourcingproject.domain.store.enums.StoreCategory;
import lombok.Getter;

@Getter
public class StoreCustomerResponseDto {

    private final String name;
    private final String address;
    private final String storePhone;
    private final StoreCategory category;
    private final int minPrice;
    private final double rating;

    public StoreCustomerResponseDto(String name, String address, String storePhone, StoreCategory category, int minPrice, double rating) {
        this.name = name;
        this.address = address;
        this.storePhone = storePhone;
        this.category = category;
        this.minPrice = minPrice;
        this.rating = rating;
    }

    public static StoreCustomerResponseDto toDto(Store store) {
        return new StoreCustomerResponseDto(
                store.getName(),
                store.getAddress(),
                store.getStorePhone(),
                store.getCategory(),
                store.getMinPrice(),
                store.getRating()
        );
    }
}
