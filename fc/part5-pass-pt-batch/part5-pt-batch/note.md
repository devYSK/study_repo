# PT 이용권 서비스

Spring Batch



• 스프링에서 제공하는 특성 그대로 사용 가능

* != 스케줄러
* Application: 사용자 코드 및 구성
*  Core: Job, Step, JobLauncher, JobParameter
* Infrastructure: File, DB

![image-20230617215328205](./images//image-20230617215328205.png)



## 이용권 서비스 요구사항
## 이용권, 수업

• 사용자는 N개의 이용권을 가질 수 있다.
• 이용권은 횟수가 모두 소진되거나 이용기간이 지나면 만료된다.
• 이용권 만료 전 사용자에게 알림을 준다.
• 업체에서 원하는 시간을 설정하여 일괄로 사용자에게 이용권을 지급할 수 있다.
• 예약된 수업 10분 전 출석 안내 알람을 준다.
• 수업 종료 시간 시점 수업을 예약한 학생의 이용권 횟수를 일괄로 차감한다. (-1)



## 통계
• 사용자의 수업 예약, 출석, 이용권 횟수 등의 데이터로 유의미한 통계 데이터를 만든다.

![image-20230617215657008](./images//image-20230617215657008.png)



# 배치 구조 설계

## Step

* 배치 처리를 정의하고 제어하는 독립된 작업의 단위
*  간단히 정의한 하나의 작업 처리

![image-20230618135121199](./images//image-20230618135121199.png)

## Job

처음부터 끝까지 독립적으로 실행할 수 있으며 고유하고 순서가 지정된 여러 스텝들의 모음

![image-20230618135302143](./images//image-20230618135302143.png)

![image-20230618142510255](./images//image-20230618142510255.png)



# 배치 구조 설계

## 이용권 만료

![image-20230618145355067](./images//image-20230618145355067.png)



## 이용권 일괄 지급 JOB

![image-20230618145406463](./images//image-20230618145406463.png)

## 예약 수업 전 알람
![image-20230618145423768](./images//image-20230618145423768.png)

대량의 데이터 병렬처리 고려 

## 수업 종료 후 이용권 차감

![image-20230618145521078](./images//image-20230618145521078.png)



# ItemReader

![image-20230618165037987](./images//image-20230618165037987.png)

# Cursor Vs Paging

![image-20230618165054726](./images//image-20230618165054726.png)

# Batch 이용권 일괄 지급 Job

```java

@Configuration
public class ExpirePassesJobConfig {

	private final int CHUNK_SIZE = 5;

	// @EnableBatchProcessing로 인해 Bean으로 제공된 JobBuilderFactory, StepBuilderFactory
	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final EntityManagerFactory entityManagerFactory;
	private final PackageRepository packageRepository;

	public Job expirePassesJob() {
		// step이 1개인 Job
		return jobBuilderFactory.get("expirePassesJob")
			.start(expirePassesStep())
			.build();
	}

	@Bean
	public Step expirePassesStep() {

		return this.stepBuilderFactory.get("expirePassesStep")
			.<PassEntity, PassEntity>chunk(CHUNK_SIZE) // 작개 쪼개서 씀. 원래는 적게 안한다.
			.reader(expirePassesItemReader())
			.processor(expirePassesItemProcessor())
			.writer(expirePassesItemWriter())
			.build();

	}

	/**
	 * JpaCursorItemReader: JpaPagingItemReader만 지원하다가 Spring 4.3에서 추가되었습니다.
	 * 페이징 기법보다 보다 높은 성능으로, 데이터 변경에 무관한 무결성 조회가 가능합니다.
	 */
	@Bean
	@StepScope
	public JpaCursorItemReader<PassEntity> expirePassesItemReader() {
		return new JpaCursorItemReaderBuilder<PassEntity>()
			.name("expirePassesItemReader")
			.entityManagerFactory(entityManagerFactory)
			// 상태(status)가 진행중이며, 종료일시(endedAt)이 현재 시점보다 과거일 경우 만료 대상이 됩니다.
			.queryString("select p from PassEntity p where p.status = :status and p.endedAt <= :endedAt")
			.parameterValues(Map.of("status", PassStatus.IN_PROGRESS, "endedAt", LocalDateTime.now()))
			.build();
	}

	@Bean
	public ItemProcessor<PassEntity, PassEntity> expirePassesItemProcessor() {
		return passEntity -> {
			passEntity.setStatus(PassStatus.EXPIRED);
			passEntity.setExpiredAt(LocalDateTime.now());
			return passEntity;
		};
	}

	/**
	 * JpaItemWriter: JPA의 영속성 관리를 위해 EntityManager를 필수로 설정해줘야 합니다.
	 */
	@Bean
	public JpaItemWriter<PassEntity> expirePassesItemWriter() {
		return new JpaItemWriterBuilder<PassEntity>()
			.entityManagerFactory(entityManagerFactory)
			.build();
	}

}

```



# Tasklet -> Repeat와 RepeatStatus

Spring Batch는 Step과 그 하위 Chunk의 반족 작업이며 Repeat 정책을 따른다

![image-20230618185002241](./images//image-20230618185002241.png)



# 멀티스레드 기반 예약 수업 전 알람

![image-20230621132338024](./images//image-20230621132338024.png)





# Spring Bean @JobScope @StepScope

- ﻿﻿Spring Bean의 기본 Scope =  Singleton
- ﻿﻿Bean의 생성 시점을 지정된 Scope가 명시된 method가 실행되는 시점으로 지연
  -  JobScope Job이 실행될 때 생성되고 끝날 때 삭제
  -  StepScope Step이 실행될 때 생성되고 끝날 때 삭제
- ﻿﻿Why?

1. ﻿﻿﻿JobParameter를 method 실행하는 시점까지 지연시켜 할당할 수 있다.
2. ﻿﻿﻿동일한 Component를 병렬로 처리할 때 안전할 수 있다.

![image-20230621183030449](./images//image-20230621183030449.png)



# Rest API로 Job 실행하기 



```java
package org.springframework.batch.core.launch;
public interface JobLauncher {
	// 실행할 잡 및 잡에 전달할 잡 파라미터를 받아 잡을 실행시킨다.
	JobExecution run(Job job, JobParameters jobParameters) throws 
    JobExecutionAlreadyRunningException, 
  JobRestartException, 
  JobInstanceAlreadyCompleteException, 
  JobParametersInvalidException;
}
```

```sh
$
curl -X POST 'http://localhost:8081/job/launcher' 
-H 'Content-Type: application/json' -d 
'{
  "name" : "makeStatisticsJob", 
  "jobParameters": { "from":"2022-09-01 00:00", "to":"2022-09-03 00:00" } 
}'
{"exitCode":"COMPLETED","exitDescription":"","running":false}
```

```java
@EnableBatchProcessing
@Configuration
public class BatchConfig {

    /**
     * JobRegistry는 context에서 Job을 추적할 때 유용합니다.
     * JobRegistryBeanPostProcessor는 Application Context가 올라가면서 bean 등록 시, 자동으로 JobRegistry에 Job을 등록 시켜줍니다.
     */
    @Bean
    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(JobRegistry jobRegistry) {
        JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor = new JobRegistryBeanPostProcessor();
        jobRegistryBeanPostProcessor.setJobRegistry(jobRegistry);
        return jobRegistryBeanPostProcessor;

    }
}


// JobRegistryBeanPostProcessor가 등록되어야 jobRegistry를 사용 가능 
@RestController
@RequestMapping("job")
public class JobLauncherController {

    private final JobLauncher jobLauncher;

    private final JobRegistry jobRegistry;

    public JobLauncherController(JobLauncher jobLauncher, JobRegistry jobRegistry) {
        this.jobLauncher = jobLauncher;
        this.jobRegistry = jobRegistry;
    }

    @PostMapping("/launcher")
    public ExitStatus launchJob(@RequestBody JobLauncherRequest request) throws Exception {
        Job job = jobRegistry.getJob(request.getName());
        return this.jobLauncher.run(job, request.getJobParameters()).getExitStatus();

    }
}

@Getter
@Setter
@ToString
public class JobLauncherRequest {
    private String name;
    private Properties jobParameters;

    public JobParameters getJobParameters() {
        return new JobParametersBuilder(this.jobParameters).toJobParameters();

    }

}
//
package java.util;

public class Properties extends Hashtable<Object,Object> {
  
}
```

