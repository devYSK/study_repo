package com.nhnent.forward.mybatistojpa.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:/properties/jdbc.properties")
// NOTE #2 : 클래스패스를 지정하여 mapper interface를 자동 스캔
@MapperScan("com.nhnent.forward.mybatistojpa.mapper")
@EnableTransactionManagement
public class MybatisConfig {
    @Value("${jdbc.driverClassName}")
    private String driveClassName;

    @Value("${jdbc.url}")
    private String url;

    @Value("${jdbc.username}")
    private String username;

    @Value("${jdbc.password}")
    private String password;


    @Bean
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(driveClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource);

        return transactionManager;
    }

    // NOTE #3 : mybatis-spring에서 SqlSessionFactory를 생성하기 위해 SqlSessionFactoryBean 사용
    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean(DataSource dataSource) {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        // NOTE #4 : 자바 모델 클래스의 typeAlias를 스캔할 패키지 지정
        sqlSessionFactoryBean.setTypeAliasesPackage("com.nhnent.forward.mybatistojpa.model");

        return sqlSessionFactoryBean;
    }

    @Primary
    @Bean
    public SqlSessionFactory sqlSessionFactory(SqlSessionFactoryBean sqlSessionFactoryBean) throws Exception {
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBean.getObject();
        // NOTE #5 : 데이터베이스 컬럼명에 쓰여진 underscore("_")를 자바 모델 클래스의 속성명에서는 camelCase로 자동으로 맵핑
        sqlSessionFactory.getConfiguration().setMapUnderscoreToCamelCase(true);

        return sqlSessionFactory;
    }

}
