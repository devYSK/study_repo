package com.ys.jwtbasic.core.mvc;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ys.jwtbasic.next.controller.*;
import com.ys.jwtbasic.next.controller.qna.AddAnswerController;
import com.ys.jwtbasic.next.controller.qna.DeleteAnswerController;
import com.ys.jwtbasic.next.controller.qna.ShowController;

public class RequestMapping {
	private static final Logger logger = LoggerFactory.getLogger(RequestMapping.class);
	private Map<String, Controller> mappings = new HashMap<>();

	void initMapping() {
		mappings.put("/", new HomeController());
		mappings.put("/users/form", new ForwardController("/user/form.jsp"));
		mappings.put("/users/loginForm", new ForwardController("/user/login.jsp"));
		mappings.put("/users", new ListUserController());
		mappings.put("/users/login", new LoginController());
		mappings.put("/users/profile", new ProfileController());
		mappings.put("/users/logout", new LogoutController());
		mappings.put("/users/create", new CreateUserController());
		mappings.put("/users/updateForm", new UpdateFormUserController());
		mappings.put("/users/update", new UpdateUserController());
		mappings.put("/qna/form", new ForwardController("/qna/form.jsp"));
		mappings.put("/qna/show", new ShowController());
		mappings.put("/api/qna/addAnswer", new AddAnswerController());
		mappings.put("/api/qna/deleteAnswer", new DeleteAnswerController());

		logger.info("Initialized Request Mapping!");
	}

	public Controller findController(String url) {
		return mappings.get(url);
	}

	void put(String url, Controller controller) {
		mappings.put(url, controller);
	}
}