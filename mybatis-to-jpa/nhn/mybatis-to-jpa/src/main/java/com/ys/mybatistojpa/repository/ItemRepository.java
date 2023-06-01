package com.ys.mybatistojpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ys.mybatistojpa.entity.ItemEntity;

public interface ItemRepository extends JpaRepository<ItemEntity, Long> {
}