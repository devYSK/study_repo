package com.ys.jwtbasic.core.mvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ForwardController implements Controller {
	private String forwardUrl;

	public ForwardController(String forwardUrl) {
		this.forwardUrl = forwardUrl;
		if (forwardUrl == null) {
			throw new NullPointerException("forwardUrl is null. 이동할 URL을 입력하세요.");
		}
	}

	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		return forwardUrl;
	}
}