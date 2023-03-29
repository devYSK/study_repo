package com.ys.jwtbasic.core.mvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface Controller {

	ModelAndView execute(HttpServletRequest req, HttpServletResponse res) throws Exception;

}
