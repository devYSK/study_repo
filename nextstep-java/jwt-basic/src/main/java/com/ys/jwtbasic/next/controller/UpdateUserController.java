package com.ys.jwtbasic.next.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ys.jwtbasic.core.db.DataBase;
import com.ys.jwtbasic.core.mvc.Controller;
import com.ys.jwtbasic.next.model.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class UpdateUserController implements Controller {
	private static final Logger log = LoggerFactory.getLogger(UpdateUserController.class);

	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		User user = DataBase.findUserById(req.getParameter("userId"));
		if (!UserSessionUtils.isSameUser(req.getSession(), user)) {
			throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
		}

		User updateUser = new User(req.getParameter("userId"), req.getParameter("password"), req.getParameter("name"),
			req.getParameter("email"));
		log.debug("Update User : {}", updateUser);
		user.update(updateUser);
		return "redirect:/";
	}
}