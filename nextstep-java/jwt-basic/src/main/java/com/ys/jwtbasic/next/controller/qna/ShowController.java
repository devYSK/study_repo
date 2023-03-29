package com.ys.jwtbasic.next.controller.qna;

import com.ys.jwtbasic.core.mvc.AbstractController;
import com.ys.jwtbasic.core.mvc.Controller;
import com.ys.jwtbasic.core.mvc.ModelAndView;
import com.ys.jwtbasic.next.dao.AnswerDao;
import com.ys.jwtbasic.next.dao.QuestionDao;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ShowController extends AbstractController {

	private QuestionDao questionDao = new QuestionDao();

	private AnswerDao answerDao = new AnswerDao();

	@Override
	public ModelAndView execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		Long questionId = Long.parseLong(req.getParameter("questionId"));

		ModelAndView modelAndView = jspView("/qna/show.jsp");

		modelAndView.addObject("question", questionDao.findById(questionId));
		modelAndView.addObject("answers", answerDao.findAllByQuestionId(questionId));
		return modelAndView;
	}

}
