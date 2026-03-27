package com.example.outsourcingproject.domain.store.dto.request;

import com.example.outsourcingproject.domain.store.enums.StoreCategory;
import lombok.Getter;

@Getter
public class UpdateStoreRequestDto {

    private String name;
    private String address;
    private String storePhone;
    private StoreCategory category;
    private Integer minPrice;

}
