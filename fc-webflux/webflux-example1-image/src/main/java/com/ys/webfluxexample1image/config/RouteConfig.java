package com.ys.webfluxexample1image.config;

import static org.springframework.web.reactive.function.server.RouterFunctions.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;

import com.ys.webfluxexample1image.handler.ImageHandler;

@Configuration
public class RouteConfig {
    @Bean
    RouterFunction router(
            ImageHandler imageHandler
    ) {
        return route()
                .path("/api", b1 -> b1
                        .path("/images", b2 -> b2
                                .GET("/{imageId:[0-9]+}", imageHandler::getImageById)
                        )
                ).build();
    }
}
