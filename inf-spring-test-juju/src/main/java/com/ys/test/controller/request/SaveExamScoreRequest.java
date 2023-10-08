package com.ys.test.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

public record SaveExamScoreRequest(
	String studentName,
	Integer korScore,
	Integer englishScore,
	Integer mathScore) {
}