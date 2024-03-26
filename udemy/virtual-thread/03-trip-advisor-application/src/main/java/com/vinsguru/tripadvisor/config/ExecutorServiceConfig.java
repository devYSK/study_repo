package com.vinsguru.tripadvisor.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.springframework.boot.autoconfigure.condition.ConditionalOnThreading;
import org.springframework.boot.autoconfigure.thread.Threading;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExecutorServiceConfig {

    @Bean
    @ConditionalOnThreading(Threading.VIRTUAL)
    public ExecutorService virtualThreadExecutor(){
        final ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();
        ThreadFactory factory = Thread.ofVirtual().name("my-virtual").factory();

        return Executors.newThreadPerTaskExecutor(factory);
    }

    @Bean
    @ConditionalOnThreading(Threading.PLATFORM)
    public ExecutorService platformThreadExecutor(){
        return Executors.newCachedThreadPool();
    }

}
