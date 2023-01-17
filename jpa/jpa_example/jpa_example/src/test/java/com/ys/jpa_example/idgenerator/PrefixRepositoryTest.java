package com.ys.jpa_example.idgenerator;

import static org.junit.jupiter.api.Assertions.*;

import com.ys.jpa_example.CustomDataJpaTest;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@CustomDataJpaTest
class PrefixRepositoryTest {

    @Autowired
    private PrefixRepository prefixRepository;

    @Test
    void test() {
        PrefixEntity entity1 = new PrefixEntity();

        PrefixEntity save = prefixRepository.save(entity1);

        System.out.println("\n----\n");

        PrefixEntity entity2 = new PrefixEntity();

        PrefixEntity save2 = prefixRepository.save(entity2);


        System.out.println("\n----\n");


        PrefixEntity entity3 = new PrefixEntity();

        PrefixEntity save3 = prefixRepository.save(entity3);


        prefixRepository.flush();;

        List<PrefixEntity> all = prefixRepository.findAll();

        System.out.println(all);


    }

}