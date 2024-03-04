package com.ys.apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.ys.apigateway.filter.CustomFilter;

// @Configuration
public class FilterConfig {
    Environment env;

    public FilterConfig(Environment env) {
        this.env = env;
    }

   @Bean
   public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
       // , CustomFilter customFilter) {

       return builder.routes()
           .route(r -> r.path("/first-service/**")
               .filters(f -> f.addRequestHeader("first-request", "first-request-header-by-java")
                       .addResponseHeader("first-response", "first-response-header-by-java")
                       // .filter(customFilter.apply(new CustomFilter.Config()))
                   //                                           .filter(myfilter.apply(new AuthorizationHeaderFilter.Config()))
               )
               .uri("http://localhost:8081"))
           .route(r -> r.path("/second-service/**")
               .filters(f -> f.addRequestHeader("second-request", "second-request-header-by-java")
                   .addResponseHeader("second-response", "second-response-header-by-java"))
               .uri("http://localhost:8082"))
           .build();
   }

}
