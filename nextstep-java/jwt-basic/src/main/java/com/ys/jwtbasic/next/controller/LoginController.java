package com.ys.jwtbasic.next.controller;

import com.ys.jwtbasic.core.db.DataBase;
import com.ys.jwtbasic.core.mvc.Controller;
import com.ys.jwtbasic.next.model.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class LoginController implements Controller {

	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		String userId = req.getParameter("userId");
		String password = req.getParameter("password");
		User user = DataBase.findUserById(userId);
		if (user == null) {
			req.setAttribute("loginFailed", true);
			return "/user/login.jsp";
		}
		if (user.matchPassword(password)) {
			HttpSession session = req.getSession();
			session.setAttribute(UserSessionUtils.USER_SESSION_KEY, user);
			return "redirect:/";
		} else {
			req.setAttribute("loginFailed", true);
			return "/user/login.jsp";
		}
	}
}
