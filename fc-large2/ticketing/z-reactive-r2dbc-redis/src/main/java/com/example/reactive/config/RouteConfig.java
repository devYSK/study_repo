package com.example.reactive.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.example.reactive.SampleHandler;

@Configuration
@RequiredArgsConstructor
public class RouteConfig {
    private final SampleHandler sampleHandler;

    @Bean
    public RouterFunction<ServerResponse> route() {
        return RouterFunctions.route()
                .GET("/hello-functional", sampleHandler::getString)
                .build();
    }
}
