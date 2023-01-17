package com.ys.jpa_example.idgenerator;

import com.ys.jpa_example.CustomDataJpaTest;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@CustomDataJpaTest
class SimpleRepositoryTest {

    @Autowired
    private SimpleRepository simpleRepository;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private UUIDRepository uuidRepository;

    @Autowired
    private RandomIdRepository randomIdRepository;


    @DisplayName("tt")
    @Test
    void test() {
        //given

        SimpleEntity title = SimpleEntity.generate("title");

        SimpleEntity save = simpleRepository.save(title);
        simpleRepository.flush();
        //when

        //then
        System.out.println("id : " + save.getId());

        simpleRepository.findById(save.getId());
//        simpleRepository.findById("hihi");
    }


    @Test
    void test2() {
        UUIDEntity uuidEntity = new UUIDEntity("title");

        uuidRepository.saveAndFlush(uuidEntity);

        System.out.println(uuidEntity.getId());
//        em.clear();
        uuidRepository.findById(uuidEntity.getId());

    }

    @Test
    void randomTest() {
        RandomIdEntity randomIdEntity = new RandomIdEntity();

        List<RandomIdEntity> collect = IntStream.range(0, 10).mapToObj(value -> {
            return new RandomIdEntity();
        }).collect(Collectors.toList());

        randomIdRepository.save(randomIdEntity);


        randomIdRepository.saveAll(collect);
        randomIdRepository.flush();

        System.out.println(randomIdEntity.getId());

    }
}