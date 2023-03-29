package com.ys.jwtbasic.next.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ys.jwtbasic.core.mvc.Controller;
import com.ys.jwtbasic.next.dao.UserDao;
import com.ys.jwtbasic.next.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CreateUserController implements Controller {
	private static final Logger log = LoggerFactory.getLogger(CreateUserController.class);

	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		User user = new User(req.getParameter("userId"), req.getParameter("password"), req.getParameter("name"),
			req.getParameter("email"));
		log.debug("User : {}", user);

		UserDao userDao = new UserDao();

			userDao.insert(user);

		return "redirect:/";
	}

}