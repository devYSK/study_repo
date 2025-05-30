package com.ys.atotal.config;

import org.jooq.conf.ExecuteWithoutWhere;
import org.springframework.boot.autoconfigure.jooq.DefaultConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * WHERE 절이 없는 UPDATE, DELETE 쿼리 실행 차단 중요!!!!
 */
@Configuration
public class JooqConfig {

    @Bean
    public DefaultConfigurationCustomizer jooqDefaultConfigurationCustomizer() {

        return c -> {
            c.set(PerformanceListener::new);

            c.settings()
                .withExecuteDeleteWithoutWhere(ExecuteWithoutWhere.THROW)
                .withExecuteUpdateWithoutWhere(ExecuteWithoutWhere.THROW)
                .withRenderSchema(false)
            // implicit path join to-many는 기본적으로 에러를 발생시켜 이렇게 수동으로 해야한다.
            //                 .withRenderImplicitJoinToManyType(RenderImplicitJoinType.INNER_JOIN)

            // implicit PATH JOIN many-to-one 을 비활성화 하고 싶다면 하고 싶다면
            //                 .withRenderImplicitJoinType(RenderImplicitJoinType.THROW)
            ;
        };
    }
}