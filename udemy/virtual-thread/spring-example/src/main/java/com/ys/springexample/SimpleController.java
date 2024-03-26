package com.ys.springexample;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class SimpleController {

	private final SimpleService simpleService;


	@GetMapping("/")
	public String hello() {
		simpleService.call();
		return "Hello, Spring!";
	}

}
