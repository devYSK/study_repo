package com.yscorp.mongobatch

import org.springframework.batch.support.transaction.ResourcelessTransactionManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager

/**
 * Spring Boot 3.5 + Spring Batch 5.x 구성
 *
 * - JDBC 없이 인메모리 JobRepository를 사용할 때는 application.yml의
 *   `spring.batch.job.repository.type=memory` 설정만으로
 *   JobRepository/JobLauncher/JobExplorer가 자동 구성됨.
 * - 여기서는 청크 트랜잭션을 위한 최소한의 TransactionManager만 제공한다.
 */
@Configuration
class BatchInfrastructureConfig {

    /** 인메모리 배치 실행용 리소스리스 트랜잭션 매니저 */
}