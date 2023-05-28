package hello.servlet;

import java.io.IOException;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * http://localhost:8080/test
 */
@WebServlet(urlPatterns = "/test")
public class TestServlet extends HttpServlet {
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
		throws IOException {
		System.out.println("TestServlet.service");
		resp.getWriter().println("test");
	}
}
