package com.ys.test;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.ys.test.model.StudentScore;
import com.ys.test.repository.StudentScoreRepository;

import jakarta.persistence.EntityManager;

class TestApplicationTests extends IntegrationTest{


	@Autowired
	private StudentScoreRepository studentScoreRepository;

	@Autowired
	private EntityManager entityManager;

	@Transactional
	@Test
	void contextLoads() {

		final var exam = StudentScore.builder()
									 .mathScore(60)
									 .exam("exam")
									 .korScore(100)
									 .mathScore(90)
									 .englishScore(33)
									 .build();

		studentScoreRepository.save(exam);

		entityManager.flush();
		entityManager.clear();

		final var byId = studentScoreRepository.findById(exam.getId());

		System.out.println(byId.get().getExam());

	}


	@Test
	void test() {

	}
}
