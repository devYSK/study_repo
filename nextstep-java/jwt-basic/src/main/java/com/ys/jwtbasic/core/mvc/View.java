package com.ys.jwtbasic.core.mvc;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface View {

	void render(Map<String, ?> model,
		HttpServletRequest request,
		HttpServletResponse response) throws Exception;

}
