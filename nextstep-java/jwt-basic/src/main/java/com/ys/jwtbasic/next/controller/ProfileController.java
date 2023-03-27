package com.ys.jwtbasic.next.controller;

import com.ys.jwtbasic.core.db.DataBase;
import com.ys.jwtbasic.core.mvc.Controller;
import com.ys.jwtbasic.next.model.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ProfileController implements Controller {
	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		String userId = req.getParameter("userId");
		User user = DataBase.findUserById(userId);
		if (user == null) {
			throw new NullPointerException("사용자를 찾을 수 없습니다.");
		}
		req.setAttribute("user", user);
		return "/user/profile.jsp";
	}
}