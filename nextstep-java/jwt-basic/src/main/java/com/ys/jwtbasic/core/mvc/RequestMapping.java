package com.ys.jwtbasic.core.mvc;

import java.util.HashMap;
import java.util.Map;

import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import com.ys.jwtbasic.next.controller.*;

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

		logger.debug("Initialized Request Mapping!");
	}

	public Controller findController(String url) {
		return mappings.get(url);
	}

	void put(String url, Controller controller) {
		mappings.put(url, controller);
	}
}