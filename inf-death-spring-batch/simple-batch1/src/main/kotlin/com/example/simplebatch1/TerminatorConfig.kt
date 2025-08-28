package com.example.simplebatch1

import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Configuration
class TerminatorConfig {

    val log = org.slf4j.LoggerFactory.getLogger(TerminatorConfig::class.java)

    @Bean
    fun terminatorJob(jobRepository: JobRepository, terminationStep: Step?): Job {
        return JobBuilder("terminatorJob", jobRepository)
            .start(terminationStep)
            .build()
    }

    @Bean
    fun terminationStep(
        jobRepository: JobRepository,
        transactionManager: PlatformTransactionManager,
        terminatorTasklet: Tasklet
    ): Step {
        return StepBuilder("terminationStep", jobRepository)
            .tasklet(terminatorTasklet, transactionManager)
            .build()
    }

    @Bean
    @StepScope
    fun terminatorTasklet(
        @Value("#{jobParameters['executionDate']}") executionDate: LocalDate,
        @Value("#{jobParameters['startTime']}") startTime: LocalDateTime
    ): Tasklet {
        return Tasklet { contribution: StepContribution, chunkContext: ChunkContext ->
            log.info("시스템 처형 정보:")
            log.info("처형 예정일: {}", executionDate.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")))
            log.info("작전 개시 시각: {}", startTime.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초")))
            log.info("⚡ {}에 예정된 시스템 정리 작전을 개시합니다.", executionDate)
            log.info("💀 작전 시작 시각: {}", startTime)


// 작전 진행 상황 추적
            var currentTime = startTime
            for (i in 1..3) {
                currentTime = currentTime.plusHours(1)
                log.info("☠️ 시스템 정리 {}시간 경과... 현재 시각:{}", i, currentTime.format(DateTimeFormatter.ofPattern("HH시 mm분")))
            }

            log.info("🎯 임무 완료: 모든 대상 시스템이 성공적으로 제거되었습니다.")
            log.info("⚡ 작전 종료 시각: {}", currentTime.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초")))
            RepeatStatus.FINISHED
        }
    }
}