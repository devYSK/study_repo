package com.ys.springexample;

import org.slf4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class SimpleController {

	private final SimpleService simpleService;

	private static final Logger log = org.slf4j.LoggerFactory.getLogger(SimpleController.class);

	@GetMapping("/")
	public String hello() {
		simpleService.call();

		log.info("Controller method executed on thread: {}", Thread.currentThread().getName());
		return "Hello, Spring!";
	}

}
