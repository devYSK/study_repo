package com.ys.jwtbasic.next.controller.qna;

import java.io.PrintWriter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ys.jwtbasic.core.mvc.Controller;
import com.ys.jwtbasic.next.dao.AnswerDao;
import com.ys.jwtbasic.next.model.Result;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class DeleteAnswerController implements Controller {
	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		Long answerId = Long.parseLong(req.getParameter("answerId"));
		AnswerDao answerDao = new AnswerDao();

		answerDao.delete(answerId);

		ObjectMapper mapper = new ObjectMapper();
		resp.setContentType("application/json;charset=UTF-8");
		PrintWriter out = resp.getWriter();
		out.print(mapper.writeValueAsString(Result.ok()));
		return null;
	}
}
