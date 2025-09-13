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
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.transaction.PlatformTransactionManager
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

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
     * Query를 직접 작성하여 MongoCursorItemReader를 생성하는 예제입니다.
     * 이 방법은 복잡한 쿼리를 작성할 때 유용합니다.
     *  예제의 구성 코드를 보면 한 가지 이상한 점이 있다. Query 객체에서 이미 정렬 조건을 지정했는데도 sorts()메서드로 정렬 조건을 또 한 번 지정하고 있다. 이는 MongoCursorItemReaderBuilder의 코드에 불필요한 검증 로직이 존재하기 때문이다.
     * 빌더 내부에서는 jsonQuery()나 query()를 사용할 때 반드시 sorts()가 설정되었는지 확인한다.
     *
     * 실제로는 Query 객체에 설정된 정렬 조건만 사용되고 sorts에 전달된 정렬 조건은 무시되지만, 빌더의 검증을 통과하기 위해서는 어쩔 수 없이 sorts를 추가로 설정해야 하는 상황이다.
     */
    @Bean
    @StepScope
    fun securityLogReaderQuery(
        @Value("#{jobParameters['searchDate']}") searchDate: LocalDate
    ): MongoCursorItemReader<SecurityLog> {
        val query = Query()
            .addCriteria(Criteria.where("label").`is`("PENDING_ANALYSIS"))
            .addCriteria(
                Criteria.where("timestamp")
                    .gte(Date.from(searchDate.atStartOfDay(ZoneId.systemDefault()).toInstant()))
                    .lt(Date.from(searchDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()))
            )
            .with(Sort.by(Sort.Direction.ASC, "timestamp"))
            .cursorBatchSize(10)

        return MongoCursorItemReaderBuilder<SecurityLog>()
            .name("securityLogReader")
            .template(mongoTemplate)
            .collection("security_logs")
            .query(query)
            // MongoCursorItemReaderBuilder 버그 때문에 sort 옵션도 지정 필요
            .sorts(mapOf("timestamp" to Sort.Direction.ASC))
            .targetType(SecurityLog::class.java)
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


}
