package com.example.simplebatch1

import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.io.File


@Configuration
class FileCleanupBatchConfig(
    val jobRepository: org.springframework.batch.core.repository.JobRepository,
    val transactionManager: org.springframework.transaction.PlatformTransactionManager,
) {

    // 원래 있던 JobLauncher 빈을 오버라이드
//    @Bean("asyncJobLauncher")
//    fun asyncJobLauncher(jobRepository: JobRepository?): JobLauncher {
//        val launcher = TaskExecutorJobLauncher()
//        launcher.setJobRepository(jobRepository)
//        // ThreadPool 혹은 SimpleAsync 사용
//        val executor = ThreadPoolTaskExecutor()
//        executor.setCorePoolSize(5)
//        executor.setMaxPoolSize(10)
//        executor.afterPropertiesSet()
//        launcher.setTaskExecutor(executor)
//        return launcher
//    }

    @Bean
    fun deleteOldFilesTasklet(): Tasklet {
        // "temp" 디렉토리에서 30일 이상 지난 파일 삭제
        return DeleteOldFilesTasklet("/path/to/temp", 30)
    }

    @Bean
    fun deleteOldFilesStep(): Step? {
        return StepBuilder("deleteOldFilesStep", jobRepository)
            .tasklet(deleteOldFilesTasklet(), transactionManager)
            .build()
    }

    @Bean
    fun deleteOldFilesJob(): Job {
        return JobBuilder("deleteOldFilesJob", jobRepository)
            .start(deleteOldFilesStep())
            .build()
    }
}

class DeleteOldFilesTasklet(private val path: String, private val daysOld: Int) : Tasklet {
    
    private val log = org.slf4j.LoggerFactory.getLogger(DeleteOldFilesTasklet::class.java)
    
    override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus {
        val dir: File = File(path)
        val cutoffTime = System.currentTimeMillis() - (daysOld * 24 * 60 * 60 * 1000L)

        val files: Array<File>? = dir.listFiles()

        if (files != null) {
            for (file in files) {
                if (file.lastModified() < cutoffTime) {
                    if (file.delete()) {
                        log.info("🔥 파일 삭제: {}", file.getName())
                    } else {
                        log.info("⚠️  파일 삭제 실패: {}", file.getName())
                    }
                }
            }
        }
        return RepeatStatus.FINISHED
    }
}