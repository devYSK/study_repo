package com.fastcampuspay.settlement.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.fastcampuspay.settlement.tasklet.SettlementTasklet;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SettlementJob {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final SettlementTasklet settlementTasklet;

    @Bean
    public Job settlement() {
        return jobBuilderFactory.get("settlement")
                .start(settlementStep())
                .build();
    }

    public Step settlementStep() {
        return stepBuilderFactory.get("settlementStep")
                .tasklet(settlementTasklet)
                .build();
    }
}
