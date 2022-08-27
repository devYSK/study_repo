package com.ys.test.member;

import org.h2.engine.DbSettings;
import org.h2.engine.SettingsBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.TestDatabaseAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : ysk
 */
@ActiveProfiles("test")
@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    DataSource dataSource;

    @Autowired
    EntityManager em;

    @Autowired
    TestDatabaseAutoConfiguration TestDatabaseAutoConfiguration;

    @Autowired
    Environment environment;


    @Test
    void save() throws SQLException {

        DatabaseMetaData metaData = dataSource.getConnection().getMetaData();

        Map<String, Object> properties = em.getProperties();

        Object delegate = em.getDelegate();


        System.out.println();

        memberRepository.save(
                Member.builder()
                        .age(1)
                        .email("email@email.com")
                        .name("name")
                        .build()
        );

    }
}