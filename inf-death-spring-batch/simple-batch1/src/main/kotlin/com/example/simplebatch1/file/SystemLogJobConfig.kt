package com.example.simplebatch1.file

import org.slf4j.LoggerFactory
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.Chunk
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.file.FlatFileItemReader
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder
import org.springframework.batch.item.file.mapping.FieldSetMapper
import org.springframework.batch.item.file.mapping.PatternMatchingCompositeLineMapper
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer
import org.springframework.batch.item.file.transform.FieldSet
import org.springframework.batch.item.file.transform.LineTokenizer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.FileSystemResource
import org.springframework.transaction.PlatformTransactionManager

@Configuration
class SystemLogJobConfig(
    private val jobRepository: JobRepository,
    private val transactionManager: PlatformTransactionManager
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Bean
    fun systemLogJob(systemLogStep: Step): Job =
        JobBuilder("systemLogJob", jobRepository)
            .start(systemLogStep)
            .build()

    @Bean
    fun systemLogStep(
        systemLogReader: FlatFileItemReader<SystemLog>,
        systemLogWriter: ItemWriter<SystemLog>
    ): Step =
        StepBuilder("systemLogStep", jobRepository)
            .chunk<SystemLog, SystemLog>(10, transactionManager)
            .reader(systemLogReader)
            .writer(systemLogWriter)
            .build()

    @Bean
    @StepScope
    fun systemLogReader(@Value("#{jobParameters['inputFile']}") inputFile: String): FlatFileItemReader<SystemLog> =
        FlatFileItemReaderBuilder<SystemLog>()
            .name("systemLogReader")
            .resource(FileSystemResource(inputFile))
            .lineMapper(systemLogLineMapper())
            .build()

    @Bean
    fun systemLogLineMapper(): PatternMatchingCompositeLineMapper<SystemLog> {
        val lineMapper = PatternMatchingCompositeLineMapper<SystemLog>()

        val tokenizers = mapOf(
            "ERROR*" to errorLineTokenizer(),
            "ABORT*" to abortLineTokenizer(),
            "COLLECT*" to collectLineTokenizer()
        )
        lineMapper.setTokenizers(tokenizers)

        val mappers = mapOf(
            "ERROR*" to ErrorFieldSetMapper(),
            "ABORT*" to AbortFieldSetMapper(),
            "COLLECT*" to CollectFieldSetMapper()
        )
        lineMapper.setFieldSetMappers(mappers)

        return lineMapper
    }

    @Bean
    fun errorLineTokenizer() = DelimitedLineTokenizer(",").apply {
        setNames("type", "application", "errorType", "timestamp", "message", "resourceUsage", "logPath")
    }

    @Bean
    fun abortLineTokenizer() = DelimitedLineTokenizer(",").apply {
        setNames("type", "application", "errorType", "timestamp", "message", "exitCode", "processPath", "status")
    }

    @Bean
    fun collectLineTokenizer() = DelimitedLineTokenizer(",").apply {
        setNames("type", "dumpType", "processId", "timestamp", "dumpPath")
    }

    @Bean
    fun systemLogWriter(): ItemWriter<SystemLog> = ItemWriter { items ->
        items.forEach { item -> log.info("{}", item) }
    }

    open class SystemLog(
        var type: String? = null,
        var timestamp: String? = null
    )

    data class ErrorLog(
        var application: String? = null,
        var errorType: String? = null,
        var message: String? = null,
        var resourceUsage: String? = null,
        var logPath: String? = null
    ) : SystemLog()

    data class AbortLog(
        var application: String? = null,
        var errorType: String? = null,
        var message: String? = null,
        var exitCode: String? = null,
        var processPath: String? = null,
        var status: String? = null
    ) : SystemLog()

    data class CollectLog(
        var dumpType: String? = null,
        var processId: String? = null,
        var dumpPath: String? = null
    ) : SystemLog()

    class ErrorFieldSetMapper : FieldSetMapper<SystemLog> {
        override fun mapFieldSet(fs: FieldSet): SystemLog = ErrorLog().apply {
            type = fs.readString("type")
            application = fs.readString("application")
            errorType = fs.readString("errorType")
            timestamp = fs.readString("timestamp")
            message = fs.readString("message")
            resourceUsage = fs.readString("resourceUsage")
            logPath = fs.readString("logPath")
        }
    }

    class AbortFieldSetMapper : FieldSetMapper<SystemLog> {
        override fun mapFieldSet(fs: FieldSet): SystemLog = AbortLog().apply {
            type = fs.readString("type")
            application = fs.readString("application")
            errorType = fs.readString("errorType")
            timestamp = fs.readString("timestamp")
            message = fs.readString("message")
            exitCode = fs.readString("exitCode")
            processPath = fs.readString("processPath")
            status = fs.readString("status")
        }
    }

    class CollectFieldSetMapper : FieldSetMapper<SystemLog> {
        override fun mapFieldSet(fs: FieldSet): SystemLog = CollectLog().apply {
            type = fs.readString("type")
            dumpType = fs.readString("dumpType")
            processId = fs.readString("processId")
            timestamp = fs.readString("timestamp")
            dumpPath = fs.readString("dumpPath")
        }
    }
}