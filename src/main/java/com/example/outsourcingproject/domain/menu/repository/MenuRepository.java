package com.example.outsourcingproject.domain.menu.repository;

import com.example.outsourcingproject.domain.menu.entity.Menu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    Page<Menu> findAllByStoreIdAndDeleteFlagFalse(Long storeId, Pageable pageable);

}
