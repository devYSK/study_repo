package com.ys.test.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ys.test.controller.request.SaveExamScoreRequest;
import com.ys.test.controller.response.ExamFailStudentResponse;
import com.ys.test.controller.response.ExamPassStudentResponse;
import com.ys.test.controller.service.StudentScoreService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/exam")
@RequiredArgsConstructor
public class ScoreApi {

	private final StudentScoreService studentScoreService;

	@PutMapping("/{exam}/score")
	public Object saveStudentScore(
		@PathVariable String exam,
		@RequestBody SaveExamScoreRequest request
	) {
		studentScoreService.saveScore(request.studentName(), exam, request.korScore(), request.englishScore(),
			request.mathScore());

		return request;
	}

	@GetMapping("/{exam}/pass")
	public List<ExamPassStudentResponse> getPassingStudents(@PathVariable String exam) {
		return studentScoreService.getPassStudentsList(exam);
	}

	@GetMapping("/{exam}/fail")
	public List<ExamFailStudentResponse> getFailingStudents(@PathVariable String exam) {
		return studentScoreService.getFailStudentsList(exam);
	}

}
