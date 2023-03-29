package com.ys.jwtbasic.next.controller.qna;

import java.io.PrintWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ys.jwtbasic.core.mvc.AbstractController;
import com.ys.jwtbasic.core.mvc.Controller;
import com.ys.jwtbasic.core.mvc.ModelAndView;
import com.ys.jwtbasic.next.dao.AnswerDao;
import com.ys.jwtbasic.next.model.Answer;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AddAnswerController extends AbstractController {

	private static final Logger log = LoggerFactory.getLogger(AddAnswerController.class);

	private AnswerDao answerDao = new AnswerDao();

	@Override
	public ModelAndView execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		Answer answer = new Answer(req.getParameter("writer"), req.getParameter("contents"),
			Long.parseLong(req.getParameter("questionId")));
		log.debug("answer : {}", answer);

		Answer savedAnswer = answerDao.insert(answer);
		return jsonView().addObject("answer", savedAnswer);
	}

}
