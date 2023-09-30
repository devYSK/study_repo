package dev.be.feign.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.be.feign.service.DemoService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class DemoController {

    private final DemoService demoService;

    @GetMapping("/get")
    public String getController() {
        return demoService.get();
    }

    @GetMapping("/post")
    public String postController() {
        return demoService.post();
    }

    @GetMapping("/error")
    public String errorDecoderController() {
        return demoService.errorDecoder();
    }
}
