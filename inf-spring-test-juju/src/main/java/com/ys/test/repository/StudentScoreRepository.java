package com.ys.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ys.test.model.StudentScore;

public interface StudentScoreRepository extends JpaRepository<StudentScore, Long> {
	// 필요한 쿼리 메서드를 여기에 추가...
}