package com.yscorp.mongobatch

import org.slf4j.LoggerFactory
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.data.MongoCursorItemReader
import org.springframework.batch.item.data.builder.MongoCursorItemReaderBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.annotation.Id
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.transaction.PlatformTransactionManager
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date

/**
 * 보안 로그에서 해커 패턴을 탐지하는 배치 작업 설정 클래스입니다.
 * 주어진 날짜의 로그를 MongoDB에서 읽어와 패턴 분석 후 라벨을 부여합니다.
 */
@Configuration
@Suppress("SpringJavaInjectionPointsAutowiringInspection")
class HackerPatternDetectionJob(
    private val jobRepository: JobRepository,
    private val transactionManager: PlatformTransactionManager,
    private val mongoTemplate: MongoTemplate
) {
    private val log = LoggerFactory.getLogger(this::class.java)

    /**
     * 해커 패턴 탐지 배치 잡을 정의합니다.
     */
    @Bean
    fun detectHackerPatternJob(detectHackerPatternStep: Step): Job =
        JobBuilder("detectHackerPatternJob", jobRepository)
            .start(detectHackerPatternStep)
            .build()

    /**
     * 보안 로그를 읽고 처리하며 쓰는 단계를 정의합니다.
     * 청크 사이즈는 10으로 설정되어 있습니다.
     */
    @Bean
    fun detectHackerPatternStep(
        securityLogReader: ItemReader<SecurityLog>,
        hackerPatternProcessor: ItemProcessor<SecurityLog, SecurityLog>,
        securityLogWriter: ItemWriter<SecurityLog>
    ): Step =
        StepBuilder("detectHackerPatternStep", jobRepository)
            .chunk<SecurityLog, SecurityLog>(10, transactionManager)
            .reader(securityLogReader)
            .processor(hackerPatternProcessor)
            .writer(securityLogWriter)
            .build()

    /**
     * 지정된 날짜의 보안 로그를 페이징 방식으로 읽어오는 MongoPagingItemReader 빈을 생성합니다.
     * 검색 날짜를 기준으로 "PENDING_ANALYSIS" 상태의 로그를 조회합니다.
     */
    @Bean
    @StepScope
    fun securityLogReader(@Value("#{jobParameters['searchDate']}") searchDate: LocalDate): MongoCursorItemReader<SecurityLog> {
        val startOfDay = Date.from(searchDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
        val endOfDay = Date.from(searchDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant())

        return MongoCursorItemReaderBuilder<SecurityLog>()
            .name("securityLogReader")
            .template(mongoTemplate)
            .collection("security_logs")
            .jsonQuery("{ \"label\": \"PENDING_ANALYSIS\", \"timestamp\": { \"\$gte\": ?0, \"\$lt\": ?1 } }")
            .parameterValues(listOf(startOfDay, endOfDay))
            .sorts(mapOf("timestamp" to Sort.Direction.ASC))
            .targetType(SecurityLog::class.java)
            .batchSize(10)
            .build()
    }

    /**
     * 보안 로그의 명령어를 분석하여 해커 공격 패턴 라벨을 부여하는 프로세서입니다.
     * 탐지 가능한 카테고리는 Lateral_Movement, Privilege_Escalation, Defense_Evasion, UNKNOWN 입니다.
     */
    @Bean
    fun hackerPatternProcessor(): ItemProcessor<SecurityLog, SecurityLog> =
        ItemProcessor { log ->
            val label = analyzeAttackPattern(log.command)
            log.copy(label = label)
        }

    /**
     * 탐지된 패턴 라벨을 기반으로 로그 정보를 출력하는 라이터입니다.
     * MongoDB 저장은 하지 않고 로그에만 출력합니다.
     */
    @Bean
    fun securityLogWriter(): ItemWriter<SecurityLog> =
        ItemWriter { logs ->
            logs.forEach { entry ->
                this.log.info("[패턴 탐지] ${entry.label}: $entry")
            }
        }

    /**
     * 명령어 문자열을 분석하여 공격 패턴 라벨을 반환합니다.
     *
     * - "ssh" 또는 "telnet" 포함 시 Lateral_Movement (측면 이동)
     * - "sudo" 또는 "su " 포함 시 Privilege_Escalation (권한 상승)
     * - "history -c", "rm /var/log", "killall rsyslog" 포함 시 Defense_Evasion (방어 회피)
     * - 그 외는 UNKNOWN (알 수 없음)
     */
    private fun analyzeAttackPattern(command: String): String {
        return when {
            command.contains("ssh", ignoreCase = true) || command.contains("telnet", ignoreCase = true) -> "Lateral_Movement"
            command.contains("sudo", ignoreCase = true) || command.contains("su ", ignoreCase = true) -> "Privilege_Escalation"
            command.contains("history -c", ignoreCase = true) ||
            command.contains("rm /var/log", ignoreCase = true) ||
            command.contains("killall rsyslog", ignoreCase = true) -> "Defense_Evasion"
            else -> "UNKNOWN"
        }
    }
}

/**
 * 보안 로그 도큐먼트 엔티티입니다.
 *
 * @property id 문서 ID
 * @property attackerId 공격자 ID
 * @property command 실행된 명령어
 * @property timestamp 로그 기록 시간
 * @property label 공격 패턴 라벨 ("PENDING_ANALYSIS" 등)
 */
@Document(collection = "security_logs")
data class SecurityLog(
    @Id
    var id: String? = null,

    var attackerId: String? = null,

    var command: String = "",

    var timestamp: LocalDateTime = LocalDateTime.MIN,

    var label: String = "PENDING_ANALYSIS"
)