package com.ys.test.controller.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.ys.test.repository.StudentScoreRepository;

class StudentScoreServiceMockTest {

	@Test
	void saveScore() {

		Mockito.mock(StudentScoreRepository.class);
	}

	@Test
	void getPassStudentsList() {
	}

	@Test
	void getFailStudentsList() {
	}
}