package com.example.demo.domain.direction.repository;

import com.example.demo.domain.direction.entity.Direction;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DirectionRepository extends JpaRepository<Direction, Long> {
}
