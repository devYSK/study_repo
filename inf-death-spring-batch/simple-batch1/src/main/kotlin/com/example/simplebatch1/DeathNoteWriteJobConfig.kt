package com.example.simplebatch1

import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.file.FlatFileItemWriter
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder
import org.springframework.batch.item.support.ListItemReader
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.FileSystemResource
import org.springframework.transaction.PlatformTransactionManager
import java.io.Writer
import java.util.List


@Configuration
class DeathNoteWriteJobConfig {
    @Bean
    fun deathNoteWriteJob(
        jobRepository: JobRepository,
        deathNoteWriteStep: Step?
    ): Job {
        return JobBuilder("deathNoteWriteJob", jobRepository)
            .start(deathNoteWriteStep)
            .build()
    }

    @Bean
    fun deathNoteWriteStep(
        jobRepository: JobRepository,
        transactionManager: PlatformTransactionManager,
        deathNoteListReader: ListItemReader<DeathNote>,
        deathNoteWriter: FlatFileItemWriter<DeathNote>
    ): Step {
        return StepBuilder("deathNoteWriteStep", jobRepository)
            .chunk<DeathNote, DeathNote>(10, transactionManager)
            .reader(deathNoteListReader)
            .writer(deathNoteWriter)
            .build()
    }

    @Bean
    fun deathNoteListReader(): ListItemReader<DeathNote> {
        val victims = List.of(
            DeathNote(
                "KILL-001",
                "김배치",
                "2024-01-25",
                "CPU 과부하"
            ),
            DeathNote(
                "KILL-002",
                "사불링",
                "2024-01-26",
                "JVM 스택오버플로우"
            ),
            DeathNote(
                "KILL-003",
                "박탐묘",
                "2024-01-27",
                "힙 메모리 고갈"
            )
        )

        return ListItemReader(victims)
    }

    @Bean
    @StepScope
    fun deathNoteWriter(
        @Value("#{jobParameters['outputDir']}") outputDir: String
    ): FlatFileItemWriter<DeathNote> {
        return FlatFileItemWriterBuilder<DeathNote>()
            .name("deathNoteWriter")
            .resource(FileSystemResource("$outputDir/death_notes.csv"))
            .delimited()
            .delimiter(",")
            .sourceType(DeathNote::class.java)
            .names("victimId", "victimName", "executionDate", "causeOfDeath")
            .headerCallback { writer: Writer -> writer.write("처형ID,피해자명,처형일자,사인") }
            .build()
    }

    data class DeathNote(
        val victimId: String,
        val victimName: String,
        val executionDate: String,
        val causeOfDeath: String,
    ) {

    }
}