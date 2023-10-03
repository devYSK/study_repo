package com.ys.storage.mysql.config

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@EnableTransactionManagement
@EntityScan(basePackages = ["com.ys.storage.mysql"])
@EnableJpaRepositories(basePackages = ["com.ys.storage.mysql"])
internal class CoreMysqlConfig
