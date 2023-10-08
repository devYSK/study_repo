package com.ys.test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MyCalculatorTest {

	@Test
	void add() {
		MyCalculator myCalculator = new MyCalculator();

		myCalculator.add(10.0);

		assertThat(myCalculator.getResult()).isEqualTo(10.0);
	}

	@Test
	void minus() {
	}

	@Test
	void multiply() {
	}

	@Test
	void divide() {
	}

	@Test
	public void display_name_genetation_test() {
		// 테스트 코드 ...
	}


}