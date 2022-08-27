package com.ys.test;

import com.ys.test.member.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.core.env.Environment;

import java.util.Arrays;

/**
 * @author : ysk
 */
@DataJpaTest
public class TestTest {

    @Autowired
    Environment environment;

    @Test
    void save() {

        System.out.println(Arrays.toString(environment.getActiveProfiles()));
        System.out.println(environment.getProperty("spring.config.activate.on-profile"));
        System.out.println(environment.getProperty("spring.datasource.url"));
    }
}
