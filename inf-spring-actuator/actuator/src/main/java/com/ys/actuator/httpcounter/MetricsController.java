package com.ys.actuator.httpcounter;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.micrometer.core.annotation.Counted;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
@RequiredArgsConstructor
public class MetricsController {

	private final MyHttpRequestManager myHttpRequestManager;  // 생성자 주입

	@Counted(value = "myCountedAnnotationCount")
	@GetMapping("/req")
	public String request() {
		// myHttpRequestManager.increase();  // counter 를 증가시킴
		return "ok";
	}
}