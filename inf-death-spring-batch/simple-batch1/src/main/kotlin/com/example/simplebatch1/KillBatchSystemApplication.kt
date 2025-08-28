package com.example.simplebatch1

import com.sun.tools.javac.tree.TreeInfo.args
import org.springframework.batch.core.Job
import org.springframework.batch.core.converter.DefaultJobParametersConverter
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.batch.core.explore.JobExplorer
import org.springframework.boot.ApplicationArguments
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import java.util.Properties
import kotlin.jvm.java

@SpringBootApplication
class KillBatchSystemApplication(
) {
//
//    @Bean
//    fun commandLineRunner(jobLauncher: JobLauncher, jobExplorer: JobExplorer, applicationContext: ApplicationContext): CommandLineRunner {
//        return CommandLineRunner { argss ->
//            println(">>> CommandLineRunner started with args: ${args.sourceArgs.joinToString(", ")}")
//            println("======================================\n\n\n")
//            val converter = DefaultJobParametersConverter()
//            val jobName =
//                args.getOptionValues("spring.batch.job.name")?.firstOrNull()
//                    ?: args.getOptionValues("spring.batch.job.names")?.firstOrNull()
//                    ?: throw IllegalArgumentException("No job name provided. Use --spring.batch.job.name=<jobName> or --spring.batch.job.names=<jobName>")
//
//            val job = applicationContext.getBean(jobName, Job::class.java)
//
//            // 2) spring.* 은 제외하고 Properties 생성 (기본 컨버터가 읽는다)
//            val props = Properties().apply {
//                for (name in args.optionNames) {
//                    if (name.startsWith("spring.")) continue
//                    val values = args.getOptionValues(name) ?: continue
//                    // 동일 키 여러 값이면 첫 번째만 사용 (필요시 병합 규칙 변경)
//                    put(name, values.first())
//                }
//            }
//
//            // 3) 기본 컨버터로 JobParameters 생성
//            val jobParameters = converter.getJobParameters(props)
//
//            // 4) 실행
//            val exec = jobLauncher.run(job, jobParameters)
//            println(">>> Job '${exec.jobInstance.jobName}' finished: ${exec.status}, id=${exec.id}")
//
////            var jobName: String? = null
////            for (arg in args) {
////                if (arg.startsWith("--spring.batch.job.name=")) {
////                    jobName = arg.substringAfter("=")
////                    break
////                }
////            }
////
////            val finalJobName = jobName ?: "systemTerminationSimulationJob" // 인자가 없으면 기본값 사용
////
////            val job = applicationContext.getBean(finalJobName, org.springframework.batch.core.Job::class.java)
////            val jobParameters = org.springframework.batch.core.JobParametersBuilder(jobExplorer)
////                .addLong("time", System.currentTimeMillis())
////                .toJobParameters()
////
////            try {
////                jobLauncher.run(job, jobParameters)
////            } catch (e: Exception) {
////                println("Job execution failed: ${e.message}")
////                e.printStackTrace()
////            }
//        }
//    }
}

fun main(args: Array<String>) {
    runApplication<KillBatchSystemApplication>(*args)
}
