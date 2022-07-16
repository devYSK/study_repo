package hello.itemservice.config;

import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.jpa.ItemQueryRepositoryV2;
import hello.itemservice.repository.jpa.JpaItemRepositoryV2;
import hello.itemservice.repository.jpa.SpringDataJpaItemRepository;
import hello.itemservice.service.ItemService;
import hello.itemservice.service.ItemServiceV1;
import hello.itemservice.service.ItemServiceV2;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;

/**
 * @author : ysk
 */
@Configuration
@RequiredArgsConstructor
public class SpringDataJpaConfig {

    private final SpringDataJpaItemRepository springDataJpaItemRepository;

//    @Bean
//    public ItemService itemService() {
//        return new ItemServiceV2(itemRepositoryV2(), ItemQueryRepositoryV2());
//    }
//
//    @Bean
//    public ItemRepository itemRepositoryV2() {
//        return new JpaItemRepositoryV2(springDataJpaItemRepository);
//    }

//    private final EntityManager em;
//
//    @Bean
//    public ItemQueryRepositoryV2 ItemQueryRepositoryV2() {
//        return new ItemQueryRepositoryV2(em);
//    }

}
