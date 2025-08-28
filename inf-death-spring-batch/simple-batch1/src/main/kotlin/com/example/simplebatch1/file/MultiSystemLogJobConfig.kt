package com.example.simplebatch1.file

import com.example.simplebatch1.file.SystemFailureJobConfig.SystemFailure
import com.example.simplebatch1.file.SystemFailureJobConfig.SystemFailureStdoutItemWriter
import org.slf4j.LoggerFactory
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.file.FlatFileItemReader
import org.springframework.batch.item.file.MultiResourceItemReader
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder
import org.springframework.batch.item.file.builder.MultiResourceItemReaderBuilder
import org.springframework.batch.item.file.mapping.FieldSetMapper
import org.springframework.batch.item.file.mapping.PatternMatchingCompositeLineMapper
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer
import org.springframework.batch.item.file.transform.FieldSet
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource
import org.springframework.transaction.PlatformTransactionManager
import kotlin.random.Random


@Configuration
class MultiSystemLogJobConfig(
    private val jobRepository: JobRepository,
    private val transactionManager: PlatformTransactionManager
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Bean
    fun multiSystemLogJob(systemLogStep: Step): Job =
        JobBuilder("multiSystemLogJob", jobRepository)
            .start(systemLogStep)
            .build()

    @Bean
    fun systemFailureStep(
        multiSystemFailureItemReader: MultiResourceItemReader<SystemFailure?>,
        systemFailureStdoutItemWriter: SystemFailureStdoutItemWriter
    ): Step {
        return StepBuilder("systemFailureStep", jobRepository)
            .chunk<SystemFailure?, SystemFailure?>(10, transactionManager)
            .reader(multiSystemFailureItemReader)
            .writer(systemFailureStdoutItemWriter)
            .build()
    }

    @Bean
    @StepScope
    fun multiSystemFailureItemReader(
        @Value("#{jobParameters['inputFilePath']}") inputFilePath: String?
    ): MultiResourceItemReader<SystemFailure?> {
        return MultiResourceItemReaderBuilder<SystemFailure?>()
            .name("multiSystemFailureItemReader")
            .resources(
                *arrayOf<Resource>(
                    FileSystemResource(inputFilePath + "/critical-failures.csv"),
                    FileSystemResource(inputFilePath + "/normal-failures.csv")
                )
            )
            .delegate(systemFailureFileReader())
            .build()
    }

    @Bean
    fun systemFailureFileReader(): FlatFileItemReader<SystemFailure> {
        return FlatFileItemReaderBuilder<SystemFailure>()
            .name("systemFailureFileReader")
            .delimited()
            .delimiter(",")
            .names("errorId", "errorDateTime", "severity", "processId", "errorMessage")
            .targetType(SystemFailure::class.java)
            .linesToSkip(1)
            .build()
    }

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