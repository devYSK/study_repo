package com.accounts.config;

import graphql.scalars.ExtendedScalars;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

@Configuration
public class GraphQlConfig {
    @Bean
    public RuntimeWiringConfigurer DateTimeConfigurer() {
        return wiringBuilder -> wiringBuilder.scalar(ExtendedScalars.DateTime);
    }

    @Bean
    public RuntimeWiringConfigurer CountryCodeConfigurer() {
        return wiringBuilder -> wiringBuilder.scalar(ExtendedScalars.CountryCode);
    }

    @Bean
    public RuntimeWiringConfigurer PositiveFloatConfigurer() {
        return wiringBuilder -> wiringBuilder.scalar(ExtendedScalars.PositiveFloat);
    }
}