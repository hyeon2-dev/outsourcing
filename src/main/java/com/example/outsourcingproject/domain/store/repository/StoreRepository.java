package com.example.outsourcingproject.domain.store.repository;

import com.example.outsourcingproject.domain.store.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface StoreRepository extends JpaRepository<Store, Long> {
    long countByUserIdAndClosedFalse(Long userId);

    Page<Store> findAllByUserId(Long userId, Pageable pageable);

    Optional<Store> findStoreById(Long storeId);

}
