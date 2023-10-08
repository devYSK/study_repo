package com.ys.test.controller.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ys.test.MyCalculator;
import com.ys.test.controller.response.ExamFailStudentResponse;
import com.ys.test.controller.response.ExamPassStudentResponse;
import com.ys.test.model.StudentFail;
import com.ys.test.model.StudentPass;
import com.ys.test.model.StudentScore;
import com.ys.test.repository.StudentFailRepository;
import com.ys.test.repository.StudentPassRepository;
import com.ys.test.repository.StudentScoreRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentScoreService {

	private final StudentScoreRepository studentScoreRepository;
	private final StudentFailRepository studentFailRepository;
	private final StudentPassRepository studentPassRepository;

	private static final double PASS_THRESHOLD = 60.0;
	private static final double NUM_SUBJECTS = 3.0;

	public void saveScore(String studentName, String exam, Integer korScore, Integer englishScore, Integer mathScore) {

		// 평균 점수를 계산하는 메서드를 생성하여 중복을 줄임
		double avgScore = calculateAverage(korScore, englishScore, mathScore);

		// StudentScore 저장
		final var studentScore = StudentScore.builder()
											 .exam(exam)
											 .studentName(studentName)
											 .korScore(korScore)
											 .englishScore(englishScore)
											 .mathScore(mathScore)
											 .build();
		studentScoreRepository.save(studentScore);

		// 평균 점수에 따라 합격/불합격 처리
		if (avgScore >= PASS_THRESHOLD) {
			final var studentPass = StudentPass.builder()
											   .avgScore(avgScore)
											   .studentName(studentName)
											   .exam(exam)
											   .build();
			studentPassRepository.save(studentPass);
		} else {
			final var studentFail = StudentFail.builder()
											   .avgScore(avgScore)
											   .studentName(studentName)
											   .exam(exam)
											   .build();
			studentFailRepository.save(studentFail);
		}
	}

	private double calculateAverage(Integer korScore, Integer englishScore, Integer mathScore) {
		final var myCalculator = new MyCalculator(0.0);
		return myCalculator.add(korScore.doubleValue())
						   .add(englishScore.doubleValue())
						   .add(mathScore.doubleValue())
						   .divide(NUM_SUBJECTS)
						   .getResult();
	}

	public List<ExamPassStudentResponse> getPassStudentsList(String exam) {
		return studentPassRepository.findAll()
									.stream()
									.filter(pass -> pass.getExam()
														.equals(exam))
									.map((pass) -> new ExamPassStudentResponse(pass.getStudentName(),
										pass.getAvgScore()))
									.toList();
	}

	public List<ExamFailStudentResponse> getFailStudentsList(String exam) {
		return studentFailRepository.findAll()
									.stream()
									.filter(pass -> pass.getExam()
														.equals(exam))
									.map((pass) -> new ExamFailStudentResponse(pass.getStudentName(),
										pass.getAvgScore()))
									.toList();

	}

}
