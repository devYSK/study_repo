package com.fastcam.helloapigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfiguration {
    public static final String MICROSERVICE_HOST_8080 = "http://localhost:8080";
    public static final String ECHO_HTTP_BIN = "http://httpbin.org:80";

    @Bean
    public RouteLocator helloRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("hello_route",
                        r -> r.path("/hello")
                        .uri(MICROSERVICE_HOST_8080))
                .route("rewrite_route",
                        r -> r.path("/gateway-hello")
                        .filters(f ->
                                f.rewritePath("/gateway-hello", "/microservice-hello")
                        )
                        .uri(MICROSERVICE_HOST_8080)
                )
                .route("add-header-route",
                        r -> r.path("/get")
                        .filters(f -> f.addRequestHeader("role", "hello-api"))
                        .uri(ECHO_HTTP_BIN)
                )
                .build();
    }
}
