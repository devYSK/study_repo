package com.nhnent.forward.mybatistojpa.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
@ComponentScan("com.nhnent.forward.mybatistojpa.controller")
public class WebConfig extends WebMvcConfigurerAdapter {

}
