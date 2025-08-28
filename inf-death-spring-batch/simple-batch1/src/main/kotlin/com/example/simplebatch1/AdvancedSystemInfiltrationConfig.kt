package com.example.simplebatch1

import org.springframework.batch.core.*
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
import org.springframework.stereotype.Component
import org.springframework.transaction.PlatformTransactionManager
import java.util.*


val log = org.slf4j.LoggerFactory.getLogger(AdvancedSystemInfiltrationConfig::class.java)

@Configuration
class AdvancedSystemInfiltrationConfig(
    val infiltrationPlanListener: InfiltrationPlanListener
) {

    @Bean
    fun systemInfiltrationJob(jobRepository: JobRepository, reconStep: Step, attackStep: Step): Job {
        return JobBuilder("systemInfiltrationJob", jobRepository)
            .listener(infiltrationPlanListener)
            .start(reconStep)
            .next(attackStep)
            .build()
    }

    @Bean
    fun reconStep(jobRepository: JobRepository, transactionManager: PlatformTransactionManager): Step {
        return StepBuilder("reconStep", jobRepository)
            .tasklet(
                { contribution: StepContribution, chunkContext: ChunkContext ->
                    val infiltrationPlan = chunkContext!!.stepContext
                        .jobExecutionContext["infiltrationPlan"] as MutableMap<String, Any>

                    log.info("침투 준비 단계: {}", infiltrationPlan["targetSystem"])
                    log.info("필요한 도구: {}", infiltrationPlan["requiredTools"])
                    RepeatStatus.FINISHED
                }, transactionManager
            )

            .build()
    }

    @Bean
    fun attackStep(
        jobRepository: JobRepository,
        transactionManager: PlatformTransactionManager,
        attackStepTasklet: Tasklet // 주입받은 Tasklet 사용
    ): Step {
        return StepBuilder("attackStep", jobRepository)
            .tasklet(attackStepTasklet, transactionManager)
            .build()
    }

    @Bean
    @StepScope
    fun attackStepTasklet(
        @Value("#{jobExecutionContext['infiltrationPlan']}") infiltrationPlan: MutableMap<String, Any>
    ): Tasklet {
        return Tasklet { contribution: StepContribution, chunkContext: ChunkContext ->
            log.info("시스템 공격 중: {}", infiltrationPlan.get("targetSystem"))
            log.info("목표: {}", infiltrationPlan.get("objective"))

            val rand: Random = Random()
            val infiltrationSuccess: Boolean = rand.nextBoolean()

            if (infiltrationSuccess) {
                log.info("침투 성공! 획득한 데이터: {}", infiltrationPlan.get("targetData"))
                contribution!!.stepExecution.jobExecution.executionContext
                    .put("infiltrationResult", "TERMINATED")
            } else {
                log.info("침투 실패. 시스템이 우리를 감지했다.")
                contribution!!.stepExecution.jobExecution.executionContext
                    .put("infiltrationResult", "DETECTED")
            }
            RepeatStatus.FINISHED
        }
    }
}

@Component
class InfiltrationPlanListener : JobExecutionListener {

    override fun beforeJob(jobExecution: JobExecution) {
        val infiltrationPlan = generateInfiltrationPlan()
        jobExecution.executionContext.put("infiltrationPlan", infiltrationPlan)
        log.info("새로운 침투 계획이 준비됐다: {}", infiltrationPlan.get("targetSystem"))
    }

    private fun generateInfiltrationPlan(): MutableMap<String, Any> {
        val targets = mutableListOf<String>(
            "판교 서버실", "안산 데이터센터"
        )
        val objectives = mutableListOf<String>(
            "kill -9 실행", "rm -rf 전개", "chmod 000 적용", "/dev/null로 리다이렉션"
        )
        val targetData = mutableListOf<String>(
            "코어 덤프 파일", "시스템 로그", "설정 파일", "백업 데이터"
        )
        val requiredTools = mutableListOf<String>(
            "USB 킬러", "널 바이트 인젝터", "커널 패닉 유발기", "메모리 시퍼너"
        )

        val rand: Random = Random()

        val infiltrationPlan: MutableMap<String, Any> = HashMap<String, Any>()
        infiltrationPlan.put("targetSystem", targets[rand.nextInt(targets.size)])
        infiltrationPlan.put("objective", objectives[rand.nextInt(objectives.size)])
        infiltrationPlan.put("targetData", targetData[rand.nextInt(targetData.size)])
        infiltrationPlan.put("requiredTools", requiredTools[rand.nextInt(requiredTools.size)])

        return infiltrationPlan
    }

    override fun afterJob(jobExecution: JobExecution) {
        val infiltrationResult = jobExecution.executionContext["infiltrationResult"]
        val infiltrationPlan = jobExecution.executionContext.get("infiltrationPlan") as MutableMap<String, Any>

        log.info("타겟 '{}' 침투 결과: {}", infiltrationPlan.get("targetSystem"), infiltrationResult)

        if ("TERMINATED" == infiltrationResult) {
            log.info("시스템 제거 완료. 다음 타겟 검색 중...")
        } else {
            log.info("철수한다. 다음 기회를 노리자.")
        }
    }
}