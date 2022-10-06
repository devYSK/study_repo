package com.ys.jpabookstudy;

import com.ys.jpabookstudy.member.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import java.util.List;

@SpringBootTest
class JpaBookStudyApplicationTests {

    @PersistenceContext
    EntityManager entityManager;
//
//    @Autowired
//    EntityManagerFactory entityManagerFactory;

//    @Test
//    void contextLoads() {
//    }
//
//    @Test
//    void test() {
//        entityManager.getDelegate();
//        System.out.println();
//
//        EntityManagerFactory jpaFactory = Persistence.createEntityManagerFactory("jpaFactory");
//
//        TestMember gg = new TestMember(1L, "gg");
//
//        entityManager.persist(gg);
//
//        EntityManager ee = jpaFactory.createEntityManager();
//        ee.clear();
//        ee.close();
//        System.out.println();
//
//    }
//
//    public static void logic(EntityManager em) {
//        String id = "id1";
//
//        Member member = new Member();
//        member.setId(id);
//        member.setUsername("영수");
//        member.setAge(28);
//
//        //등록, 자장
//        em.persist(member);
//
//        // 수정
//        member.setAge(20);
//
//        //한 건 조회
//        Member findMember = em.find(Member.class, id);
//
//        // 목록 조회
//
//        List<Member> members = em.createQuery("select m from Member m", Member.class).getResultList();
//
//        // 삭제
//        em.remove(member);
//    }
}
