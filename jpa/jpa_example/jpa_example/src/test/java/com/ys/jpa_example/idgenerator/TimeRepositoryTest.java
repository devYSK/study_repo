package com.ys.jpa_example.idgenerator;

import static org.junit.jupiter.api.Assertions.*;

import com.ys.jpa_example.CustomDataJpaTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@CustomDataJpaTest
class TimeRepositoryTest {

    @Autowired
    TimeRepository timeRepository;

    @Test
    void test() throws InterruptedException {
        TimeEntity timeEntity1 = new TimeEntity();

        TimeEntity save1 = timeRepository.save(timeEntity1);

        System.out.println("save1.id : " + save1.getId());
        timeRepository.flush();
        System.out.println("\n\n------------------\n\n");
        Thread.sleep(1000);

        TimeEntity timeEntity2 = new TimeEntity();

        TimeEntity save2 = timeRepository.save(timeEntity2);

        System.out.println("save1.id : " + save2.getId());

    }
}