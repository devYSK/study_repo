package com.ys.rest_docs_boot2.domain.customer;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


@DataJpaTest
class EntityLifeCycleTest {

    @Autowired
    private EntityManagerFactory emf;

    @DisplayName("영속 상태 테스트")
    @Test
    void managed() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        Customer customer = new Customer();
        customer.changeFirstName("ys");
        customer.changeLastName("king");

        // customer객체가 영속성 컨텍스트에서 관리된다.
        em.persist(customer);

        assertTrue(em.contains(customer));

        transaction.commit();
    }

    @DisplayName("준영속 상태 테스트")
    @Test
    void detached() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        Customer customer = new Customer();
        customer.changeFirstName("ys");
        customer.changeLastName("king");

        // customer객체가 영속성 컨텍스트에서 관리된다.
        em.persist(customer);

        em.detach(customer);// customer객체가 영속성 컨텍스트에서 분리 된다.

        assertFalse(em.contains(customer));

        transaction.commit();
    }

    @DisplayName("비영속 상태 테스트")
    @Test
    void removed() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        Customer customer = new Customer();
        customer.changeFirstName("ys");
        customer.changeLastName("king");

        // customer객체가 영속성 컨텍스트에서 관리된다.
        em.persist(customer);

        em.remove(customer);// customer 객체가 영속성 컨텍스트에서 분리 되고 제거된다.

        assertFalse(em.contains(customer));

        transaction.commit();
    }

    @DisplayName("병합(merge) 테스트")
    @Test
    void merged() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        Customer customer = new Customer();
        customer.changeFirstName("ys");
        customer.changeLastName("king");

        // customer객체가 영속성 컨텍스트에서 관리된다.
        em.persist(customer);
        em.flush();
        em.detach(customer); // customer객체가 영속성 컨텍스트에서 분리 된다.

        assertFalse(em.contains(customer));

        Customer mergeCustomer = em.merge(customer);// customer 객체를 영속상태로. 새 Managed Entity를 반환

        assertTrue(em.contains(mergeCustomer));
        transaction.commit();

    }

}
