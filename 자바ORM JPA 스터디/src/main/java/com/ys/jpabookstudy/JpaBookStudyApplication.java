package com.ys.jpabookstudy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

@SpringBootApplication
public class JpaBookStudyApplication {

//    public static void main(String[] args) {
//        SpringApplication.run(JpaBookStudyApplication.class, args);
//    }
//

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();     //트랜잭션 시작
            //비즈니스 로직 수행
            tx.commit();    // 트랜잭션 커밋

        } catch (Exception e) {
            tx.rollback();      // 예외 발생시 롤백
        } finally {
            em.close();         // 엔티티 매니저  종료
        }
        emf.close(); // 엔티티 매니저  종료
    }
}
