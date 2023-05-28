package com.ys.actuator.queue;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ys.actuator.httpcounter.MyHttpRequestManager;

import io.micrometer.core.annotation.Counted;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
@RequiredArgsConstructor
public class QueueMetricsController {

	private final MyQueueManagerWithTags myQueueManagerWithTags;

	@GetMapping("/push")
	public String push() {
		myQueueManagerWithTags.push();
		return "ok";
	}


	@GetMapping("/pop")
	public String pop() {
		myQueueManagerWithTags.pop();
		return "ok";
	}

	@Counted(value="myCountedAnnotationCount", extraTags = {"type", "test1"})
	@GetMapping("/counted")
	public String counted() {
		return "ok";
	}

	@Counted(value="myCountedAnnotationCount", extraTags = {"type", "test2"})
	@GetMapping("/counted2")
	public String counted2() {
		return "ok";
	}
}