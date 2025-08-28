package com.example.simplebatch1

import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.listener.ExecutionContextPromotionListener
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager


@Configuration
class SystemTerminationConfig {
    @Bean
    fun systemTerminationJob(jobRepository: JobRepository, scanningStep: Step, eliminationStep: Step): Job {
        return JobBuilder("systemTerminationJob", jobRepository)
            .start(scanningStep)
            .next(eliminationStep)
            .build()
    }

    @Bean
    fun scanningStep(
        jobRepository: JobRepository,
        transactionManager: PlatformTransactionManager
    ): Step {
        return StepBuilder("scanningStep", jobRepository)
            .tasklet({ contribution: StepContribution?, chunkContext: ChunkContext? ->
                val target = "판교 서버실"
                val stepContext = contribution!!.stepExecution.executionContext
                stepContext.put("targetSystem", target)
                log.info("타겟 스캔 완료: {}", target)
                RepeatStatus.FINISHED
            }, transactionManager)
            .listener(promotionListener())
            .build()
    }

    @Bean
    fun eliminationStep(
        jobRepository: JobRepository,
        transactionManager: PlatformTransactionManager,
        eliminationTasklet: Tasklet
    ): Step {
        return StepBuilder("eliminationStep", jobRepository)
            .tasklet(eliminationTasklet, transactionManager)
            .build()
    }

    @Bean
    @StepScope
    fun eliminationTasklet(
        @Value("#{jobExecutionContext['targetSystem']}") target: String?
    ): Tasklet {
        return Tasklet { contribution: StepContribution?, chunkContext: ChunkContext? ->
            log.info("시스템 제거 작업 실행: {}", target)
            RepeatStatus.FINISHED
        }
    }

    @Bean
    fun promotionListener(): ExecutionContextPromotionListener {
        val listener = ExecutionContextPromotionListener()
        listener.setKeys(arrayOf<String>("targetSystem"))
        return listener
    }
}
