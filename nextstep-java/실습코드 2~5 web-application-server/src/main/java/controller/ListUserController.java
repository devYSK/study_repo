package controller;

import java.util.Collection;
import java.util.Map;

import db.DataBase;
import http.HttpRequest;
import http.HttpResponse;
import model.User;
import util.HttpRequestUtils;

public class ListUserController extends AbstractController {

	@Override
	public void doGet(HttpRequest request, HttpResponse response) {
		if (!isLogin(request.getHeader("Cookie"))) {
			response.sendRedirect("/user/login.html");
			return;
		}

		Collection<User> users = DataBase.findAll();
		StringBuilder sb = new StringBuilder();
		sb.append("<table border='1'>");
		for (User user : users) {
			sb.append("<tr>");
			sb.append("<td>").append(user.getUserId()).append("</td>");
			sb.append("<td>").append(user.getName()).append("</td>");
			sb.append("<td>").append(user.getEmail()).append("</td>");
			sb.append("</tr>");
		}
		sb.append("</table>");
		response.forwardBody(sb.toString());
	}


	private boolean isLogin(String line) {
		String[] headerTokens = line.split(":");
		Map<String, String> cookies = HttpRequestUtils.parseCookies(headerTokens[1].trim());

		String value = cookies.get("logiend");

		if (value == null) {
			return false;
		}

		return Boolean.parseBoolean(value);
	}
}
