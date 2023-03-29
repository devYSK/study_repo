package com.ys.jwtbasic.core.mvc;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JsonView implements View{
	@Override
	public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws
		Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		response.setContentType("application/json;charset=UTF-8");

		response.getWriter().print(objectMapper.writeValueAsString(model));
	}

}
