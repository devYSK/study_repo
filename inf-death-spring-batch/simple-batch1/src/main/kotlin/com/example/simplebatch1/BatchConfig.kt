package com.example.simplebatch1

import org.springframework.batch.core.converter.JobParametersConverter
import org.springframework.batch.core.converter.JsonJobParametersConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType
import org.springframework.transaction.PlatformTransactionManager
import javax.sql.DataSource


@Configuration
class BatchConfig {

    // 한글로 작성하자
    // 스프링 배치 설정을 위한 데이터베이스 설정
    // H2 데이터베이스를 사용하여 배치 메타데이터를 저장합니다
    @Bean
    fun dataSource(): DataSource {
        return EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.H2)
            .addScript("org/springframework/batch/core/schema-h2.sql")
            .build()
    }

    @Bean
    fun transactionManager(): PlatformTransactionManager {
        return DataSourceTransactionManager(dataSource())
    }

    

}