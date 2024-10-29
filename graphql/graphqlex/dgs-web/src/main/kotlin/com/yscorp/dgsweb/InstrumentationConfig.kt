package com.yscorp.dgsweb

import graphql.analysis.MaxQueryComplexityInstrumentation
import graphql.analysis.MaxQueryDepthInstrumentation
import graphql.execution.instrumentation.Instrumentation
import graphql.execution.instrumentation.tracing.TracingInstrumentation
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Instrumentation은 GraphQL 서버에 대한 미들웨어 역할을 하며, 쿼리 실행의 각 단계를 가로채서 성능 모니터링, 로깅, 메트릭 수집, 에러 처리, 보안 검사, 트랜잭션 관리 등의 기능을 추가하는 데 사용됩니다.
 *
 * 주요 기능:
 * 쿼리 실행 전후 가로채기: 쿼리 실행 시작 전, 끝난 후, 각 필드 처리 전후 등의 시점에 개입할 수 있습니다.
 * 성능 모니터링: 실행 시간, 쿼리 복잡도 등을 측정하여 성능 데이터를 수집할 수 있습니다.
 * 보안 검토: 실행된 쿼리가 사용자 권한에 맞는지 검증할 수 있습니다.
 * 쿼리 변환: 요청된 쿼리나 변형을 동적으로 수정할 수 있습니다.
 * 에러 처리: 쿼리 실행 중 발생하는 에러를 포착하고, 이를 분석하거나 로그로 기록할 수 있습니다.
 *
 */
@Configuration
class InstrumentationConfig {

    @Bean
    fun tracingInstrumentation(): Instrumentation {
        return TracingInstrumentation()
    }

    @Bean
    fun maxQueryComplexityInstrumentation(): Instrumentation {
        return MaxQueryComplexityInstrumentation(8)
    }

    @Bean
    fun maxQueryDepthInstrumentation(): Instrumentation {
        return MaxQueryDepthInstrumentation(4)
    }
}
