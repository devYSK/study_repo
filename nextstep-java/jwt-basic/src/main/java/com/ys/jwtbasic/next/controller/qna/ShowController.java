package com.ys.jwtbasic.next.controller.qna;

import com.ys.jwtbasic.core.mvc.Controller;
import com.ys.jwtbasic.next.dao.AnswerDao;
import com.ys.jwtbasic.next.dao.QuestionDao;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ShowController implements Controller {
	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		Long questionId = Long.parseLong(req.getParameter("questionId"));
		QuestionDao questionDao = new QuestionDao();
		AnswerDao answerDao = new AnswerDao();
		req.setAttribute("question", questionDao.findById(questionId));
		req.setAttribute("answers", answerDao.findAllByQuestionId(questionId));
		return "/qna/show.jsp";
	}
}
