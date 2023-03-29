package com.ys.jwtbasic.core.mvc;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
	private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";

	private RequestMapping rm;

	@Override
	public void init() throws ServletException {
		rm = new RequestMapping();
		rm.initMapping();
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String requestUri = req.getRequestURI();
		logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

		Controller controller = rm.findController(requestUri);
		try {
			String viewName = controller.execute(req, resp);
			move(viewName, req, resp);
		} catch (Throwable e) {
			logger.error("Exception : {}", e);
			throw new ServletException(e.getMessage());
		}
	}

	private void move(String viewName, HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
		if (viewName.startsWith(DEFAULT_REDIRECT_PREFIX)) {
			resp.sendRedirect(viewName.substring(DEFAULT_REDIRECT_PREFIX.length()));
			return;
		}

		RequestDispatcher rd = req.getRequestDispatcher(viewName);
		rd.forward(req, resp);
	}
}