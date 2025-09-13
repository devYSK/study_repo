package com.yscorp.mongobatch

import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.data.MongoItemWriter
import org.springframework.batch.item.data.MongoPagingItemReader
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder
import org.springframework.batch.item.data.builder.MongoPagingItemReaderBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.MongoTransactionManager
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.transaction.PlatformTransactionManager
import java.time.LocalDate
import java.time.ZoneId
import java.util.*


/**
 * 쓰면 안됌. 오프셋 페이징이라 성능 하락 요소
 *
 * 조건에 맞는 데이터가 총 5개만 남은 상황에서 .skip(10)을 하라고 하니... 허공을 건너뛰려고 하는 것과 다름없다!
 */
@Configuration
class MongoPagingItemReaderExampleConfig(
    private val jobRepository: JobRepository,
    private val mongoTemplate: MongoTemplate,
) {

    val mongoTransactionManager = MongoTransactionManager(mongoTemplate.mongoDatabaseFactory)

    /**
     * 앞서 MongoItemWriter의 bulkWrite 동작을 설명하며 '하나의 작업이 실패하면 후속 작업들은 실행되지 않는다'고 설명했다. 그렇다면 이미 성공한 데이터들은 어떻게 될까?
     *
     * 예를 들어, 총 5개의 연산 중 1,2번째 연산까지는 성공했지만 3번째 연산에서 실패가 발생한 경우를 생각해보자. 이 경우 이미 성공한 1,2번째 연산의 결과는 MongoDB에 저장된 상태로 남아있게 된다.
     *
     * 그렇다. 롤백이 되지 않는 것이다.
     */
    @Bean
    fun detectHackerPatternJob2(detectHackerPatternStep: Step): Job =
        JobBuilder("detectHackerPatternJob2", jobRepository)
            .start(detectHackerPatternStep)
            .build()

    @Bean
    fun detectHackerPatternStep(
        securityLogReader: MongoPagingItemReader<SecurityLog>,
        hackerPatternProcessor: ItemProcessor<SecurityLog, SecurityLog>,
        securityLogWriter: MongoItemWriter<SecurityLog>
    ): Step =
        StepBuilder("detectHackerPatternStep", jobRepository)
            .chunk<SecurityLog, SecurityLog>(10, mongoTransactionManager)
            .reader(securityLogReader)
            .processor(hackerPatternProcessor)
            .writer(securityLogWriter)
            .build()

    @Bean
    @StepScope
    fun securityLogReader(
        @Value("#{jobParameters['searchDate']}") searchDate: LocalDate
    ): MongoPagingItemReader<SecurityLog> {
        val start = Date.from(searchDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
        val end = Date.from(searchDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant())

        val query = Query()
            .addCriteria(Criteria.where("label").`is`("PENDING_ANALYSIS"))
            .addCriteria(
                Criteria.where("timestamp")
                    .gte(start)
                    .lt(end)
            )
            .with(Sort.by(Sort.Direction.ASC, "timestamp"))

        return MongoPagingItemReaderBuilder<SecurityLog>()
            .name("securityLogReader")
            .template(mongoTemplate)
            .collection("security_logs")
            .query(query)
            // 빌더 이슈로 정렬을 한 번 더 명시
            .sorts(mapOf("timestamp" to Sort.Direction.ASC))
            .targetType(SecurityLog::class.java)
            .pageSize(10)
            .build()
    }

    @Bean
    fun hackerPatternProcessor(): ItemProcessor<SecurityLog, SecurityLog> =
        ItemProcessor { log ->
            val detected = analyzeAttackPattern(log.command)
            log.label = detected
            log
        }

    @Bean
    fun securityLogWriter(): MongoItemWriter<SecurityLog> =
        MongoItemWriterBuilder<SecurityLog>()
            .template(mongoTemplate)
            .collection("security_logs")
            .mode(MongoItemWriter.Mode.UPSERT) // 기존 문서 수정
            .build()

}