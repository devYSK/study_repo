package com.nhnent.forward.mybatistojpa.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Controller;

@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ComponentScan(basePackages = "com.nhnent.forward.mybatistojpa", excludeFilters = @ComponentScan.Filter(Controller.class))
public class RootConfig {
}
