package com.ys.jwtbasic.next.controller.qna;

import java.io.PrintWriter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ys.jwtbasic.core.jdbc.DataAccessException;
import com.ys.jwtbasic.core.mvc.AbstractController;
import com.ys.jwtbasic.core.mvc.Controller;
import com.ys.jwtbasic.core.mvc.JsonView;
import com.ys.jwtbasic.core.mvc.ModelAndView;
import com.ys.jwtbasic.next.dao.AnswerDao;
import com.ys.jwtbasic.next.model.Result;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class DeleteAnswerController extends AbstractController {

	private AnswerDao answerDao = new AnswerDao();

	@Override
	public ModelAndView execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		Long answerId = Long.parseLong(req.getParameter("answerId"));

		ModelAndView mav = jsonView();

		try {
			answerDao.delete(answerId);

			mav.addObject("result", Result.ok());
		} catch (DataAccessException e) {
			mav.addObject("result", Result.fail(e.getMessage()));
		}

		return mav;
	}
}
