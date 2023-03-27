package com.ys.jwtbasic.next.controller;

import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import com.ys.jwtbasic.core.db.DataBase;
import com.ys.jwtbasic.core.mvc.Controller;
import com.ys.jwtbasic.next.model.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CreateUserController implements Controller {
	private static final Logger log = LoggerFactory.getLogger(CreateUserController.class);

	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		User user = new User(req.getParameter("userId"), req.getParameter("password"), req.getParameter("name"),
			req.getParameter("email"));
		log.debug("User : {}", user);

		DataBase.addUser(user);
		return "redirect:/";
	}

}