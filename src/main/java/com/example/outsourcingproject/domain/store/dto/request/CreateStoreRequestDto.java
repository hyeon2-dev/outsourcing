package com.example.outsourcingproject.domain.store.dto.request;

import com.example.outsourcingproject.domain.store.enums.StoreCategory;
import lombok.Getter;

@Getter
public class CreateStoreRequestDto {

    private String name;
    private String address;
    private String storePhone;
    private StoreCategory category;
    private int minPrice;

}
