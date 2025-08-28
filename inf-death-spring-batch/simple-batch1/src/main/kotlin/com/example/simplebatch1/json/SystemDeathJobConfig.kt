package com.example.simplebatch1.json


import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.file.FlatFileItemReader
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.FileSystemResource
import org.springframework.transaction.PlatformTransactionManager

/**
 * json 라인을 읽는 batch job
 *
 * https://jsonlines.org/
 */
@Configuration
class SystemDeathJobConfig(
    private val jobRepository: JobRepository,
    private val transactionManager: PlatformTransactionManager,
    private val objectMapper: ObjectMapper, // injected
) {

    @Bean
    fun systemDeathJob(systemDeathStep: Step): Job =
        JobBuilder("systemDeathJob", jobRepository)
            .start(systemDeathStep)
            .build()

    @Bean
    fun systemDeathStep(
        systemDeathReader: FlatFileItemReader<SystemDeath>
    ): Step =
        StepBuilder("systemDeathStep", jobRepository)
            .chunk<SystemDeath, SystemDeath>(10, transactionManager)
            .reader(systemDeathReader)
            .writer { items -> items.forEach { println(it) } }
            .build()

    @Bean
    @StepScope
    fun systemDeathReader(
        @Value("#{jobParameters['inputFile']}") inputFile: String?
    ): FlatFileItemReader<SystemDeath> =
        FlatFileItemReaderBuilder<SystemDeath>()
            .name("systemDeathReader")
            .resource(FileSystemResource(requireNotNull(inputFile) { "jobParameter 'inputFile'가 필요합니다." }))
            // 한 줄에 하나의 JSON 객체가 들어있는 JSONL 파일을 읽는 방식
            .lineMapper { line, _ -> objectMapper.readValue(line, SystemDeath::class.java) }
            .build()

    data class SystemDeath(
        val command: String,
        val cpu: Int,
        val status: String
    )
}