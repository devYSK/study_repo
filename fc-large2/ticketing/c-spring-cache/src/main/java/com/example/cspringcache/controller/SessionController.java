package com.example.cspringcache.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;

@RestController
public class SessionController {

	@GetMapping("/")
	public Map<String, String> home(HttpSession session) {
		Integer visitCount = (Integer) session.getAttribute("visits");
		if (visitCount == null) {
			visitCount = 0;
		}
		session.setAttribute("visits", ++visitCount);
		return Map.of("session id", session.getId(), "visits", visitCount.toString());
	}


}
