package com.example.simplebatch1.log

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.file.FlatFileItemReader
import org.springframework.batch.item.file.FlatFileItemWriter
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder
import org.springframework.batch.item.file.MultiResourceItemReader
import org.springframework.batch.core.step.tasklet.SystemCommandTasklet
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.transaction.PlatformTransactionManager
import java.io.IOException
import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.StringJoiner
import java.util.regex.Pattern

@Configuration
class LogProcessingJobConfig(
    private val jobRepository: JobRepository,
    private val transactionManager: PlatformTransactionManager
) {

    @Bean
    fun logProcessingJob(
        createDirectoryStep: Step,
        logCollectionStep: Step,
        logProcessingStep: Step
    ): Job = JobBuilder("logProcessingJob", jobRepository)
        .start(createDirectoryStep)
        .next(logCollectionStep)
        .next(logProcessingStep)
        .build()

    @Bean
    fun createDirectoryStep(mkdirTasklet: SystemCommandTasklet): Step =
        StepBuilder("createDirectoryStep", jobRepository)
            .tasklet(mkdirTasklet, transactionManager)
            .build()

    @Bean
    @StepScope
    fun mkdirTasklet(
        @Value("#{jobParameters['date']}") date: String?
    ): SystemCommandTasklet =
        SystemCommandTasklet().apply {
            val d = requireNotNull(date) { "jobParameter 'date'가 필요합니다." }
            setWorkingDirectory(System.getProperty("user.home"))

            val collectedLogsPath = "collected_ecommerce_logs/$d"
            val processedLogsPath = "processed_logs/$d"

            // setCommand는 단일 문자열을 기대하므로 /bin/sh -c로 래핑
            val cmd = "mkdir -p \"$collectedLogsPath\" \"$processedLogsPath\" && ls -al"
            setCommand("/bin/sh -c \"$cmd\"")
            setTimeout(3_000) // 3초
        }

    @Bean
    fun logCollectionStep(scpTasklet: SystemCommandTasklet): Step =
        StepBuilder("logCollectionStep", jobRepository)
            .tasklet(scpTasklet, transactionManager)
            .build()

    @Bean
    @StepScope
    fun scpTasklet(
        @Value("#{jobParameters['date']}") date: String?
    ): SystemCommandTasklet =
        SystemCommandTasklet().apply {
            val d = requireNotNull(date) { "jobParameter 'date'가 필요합니다." }
            setWorkingDirectory(System.getProperty("user.home"))
            val processedLogsPath = "collected_ecommerce_logs/$d"

            val commandBuilder = StringJoiner(" && ")
            listOf("localhost").forEach { host ->
                val one = "scp $host:~/ecommerce_logs/$d.log ./$processedLogsPath/$host.log"
                commandBuilder.add(one)
            }
            // 여러 명령 연결: /bin/sh -c로 실행
            setCommand("/bin/sh -c \"${commandBuilder.toString()}\"")
            setTimeout(10_000) // 10초
        }

    @Bean
    fun logProcessingStep(
        multiResourceItemReader: MultiResourceItemReader<LogEntry>,
        logEntryProcessor: LogEntryProcessor,
        processedLogEntryJsonWriter: FlatFileItemWriter<ProcessedLogEntry>
    ): Step =
        StepBuilder("logProcessingStep", jobRepository)
            .chunk<LogEntry, ProcessedLogEntry>(10, transactionManager)
            .reader(multiResourceItemReader)
            .processor(logEntryProcessor)
            .writer(processedLogEntryJsonWriter)
            .build()

    @Bean
    @StepScope
    fun multiResourceItemReader(
        @Value("#{jobParameters['date']}") date: String?
    ): MultiResourceItemReader<LogEntry> =
        MultiResourceItemReader<LogEntry>().apply {
            val d = requireNotNull(date) { "jobParameter 'date'가 필요합니다." }
            setName("multiResourceItemReader")
            setResources(getResources(d))
            setDelegate(logFileReader())
        }

    private fun getResources(date: String): Array<Resource> =
        try {
            val userHome = System.getProperty("user.home")
            val location = "file:$userHome/collected_ecommerce_logs/$date/*.log"
            PathMatchingResourcePatternResolver().getResources(location)
        } catch (e: IOException) {
            throw RuntimeException("Failed to resolve log files", e)
        }

    @Bean
    fun logFileReader(): FlatFileItemReader<LogEntry> =
        FlatFileItemReaderBuilder<LogEntry>()
            .name("logFileReader")
            .delimited()
            .delimiter(",")
            .names("dateTime", "level", "message")
            .targetType(LogEntry::class.java)
            .build()

    @Bean
    fun logEntryProcessor(): LogEntryProcessor = LogEntryProcessor()

    @Bean
    @StepScope
    fun processedLogEntryJsonWriter(
        @Value("#{jobParameters['date']}") date: String?
    ): FlatFileItemWriter<ProcessedLogEntry> {
        val d = requireNotNull(date) { "jobParameter 'date'가 필요합니다." }
        val userHome = System.getProperty("user.home")
        val outputPath =
            Paths.get(userHome, "processed_logs", d, "processed_logs.jsonl").toString()

        val objectMapper = ObjectMapper().apply {
            val javaTimeModule = JavaTimeModule().apply {
                addSerializer(
                    LocalDateTime::class.java,
                    LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
                )
            }
            registerModule(javaTimeModule)
        }

        return FlatFileItemWriterBuilder<ProcessedLogEntry>()
            .name("processedLogEntryJsonWriter")
            .resource(FileSystemResource(outputPath))
            .lineAggregator { item ->
                try {
                    objectMapper.writeValueAsString(item)
                } catch (e: JsonProcessingException) {
                    throw RuntimeException("Error converting item to JSON", e)
                }
            }
            .build()
    }

    // ----- 모델 & 프로세서 -----

    data class LogEntry(
        var dateTime: String? = null,
        var level: String? = null,
        var message: String? = null
    )

    data class ProcessedLogEntry(
        var dateTime: LocalDateTime? = null,
        var level: LogLevel = LogLevel.UNKNOWN,
        var message: String? = null,
        var errorCode: String? = null
    )

    enum class LogLevel {
        INFO, WARN, ERROR, DEBUG, UNKNOWN;

        companion object {
            fun fromString(level: String?): LogLevel =
                if (level.isNullOrBlank()) UNKNOWN
                else runCatching { valueOf(level.trim().uppercase()) }.getOrElse { UNKNOWN }
        }
    }

    class LogEntryProcessor : ItemProcessor<LogEntry, ProcessedLogEntry> {
        private val isoFormatter: DateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME
        private val errorCodePattern: Pattern = Pattern.compile("ERROR_CODE\\[(\\w+)]")

        override fun process(item: LogEntry): ProcessedLogEntry =
            ProcessedLogEntry(
                dateTime = parseDateTime(item.dateTime),
                level = LogLevel.fromString(item.level),
                message = item.message,
                errorCode = extractErrorCode(item.message)
            )

        private fun parseDateTime(dateTime: String?): LocalDateTime? =
            dateTime?.let { LocalDateTime.parse(it, isoFormatter) }

        private fun extractErrorCode(message: String?): String? {
            if (message == null) return null
            val matcher = errorCodePattern.matcher(message)
            if (matcher.find()) return matcher.group(1)
            return if (message.contains("ERROR")) "UNKNOWN_ERROR" else null
        }
    }
}
