package com.fastcam.helloapigateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
class OrderApiRouteLocator {
    @Value("${route.order-api.v1.base-url}") String orderBaseUrl;
    private final String gatewayPath = "/providers/order-api/v1/";

    @Bean
    public RouteLocator customOrderApiRouteLocator(RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder.routes()
                .route("order-api",
                        r -> r.path(gatewayPath + "**")
                        .filters(f ->
                                f.rewritePath(gatewayPath + "(?<servicePath>.*)", "/${servicePath}")
                        ).uri(orderBaseUrl)
                )
                .build();
    }
    // GET http://localhost:9090/providers/order-api/v1/orders
}