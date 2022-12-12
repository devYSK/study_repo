package com.ys.rest_docs_boot2.domain.customer;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        customerRepository.deleteAll();
    }

    @DisplayName("insert 테스트 - 데이터를 저장하면 아이디가 생성되고 findById로 조회할 수 있다")
    @Test
    void insert() {
        //given
        String firstName = "ys";
        String lastName = "kim";
        Customer customer = new Customer(firstName, lastName);
        //when
        Customer savedCustomer = customerRepository.save(customer);

        //then
        assertNotEquals(0, savedCustomer.getId());
        Optional<Customer> customerOptional = customerRepository.findById(savedCustomer.getId());
        assertTrue(customerOptional.isPresent());
        Customer findCustomer = customerOptional.get();

        assertEquals(firstName, findCustomer.getFirstName());
        assertEquals(lastName, findCustomer.getLastName());
    }

    @DisplayName("update 테스트 - 저장된 customer의 이름이 바뀐다.")
    @Test
    void update() {
        //given
        String firstName = "ys";
        String lastName = "kim";
        String changeFirstName = "star";
        Customer customer = new Customer(firstName, lastName);
        Customer savedCustomer = customerRepository.save(customer);

        // when
        Customer findCustomer =
            assertDoesNotThrow(() -> customerRepository.findById(savedCustomer.getId()).get());

        assertDoesNotThrow(() -> findCustomer.changeFirstName(changeFirstName));

        //then
        assertEquals(changeFirstName, findCustomer.getFirstName());
        assertEquals(lastName, findCustomer.getLastName());
    }

    @DisplayName("delete test - 저장된 customer가 삭제된다.")
    @Test
    void delete() {
        //given
        String firstName = "ys";
        String lastName = "kim";
        Customer customer = new Customer(firstName, lastName);
        Customer savedCustomer = customerRepository.save(customer);

        // when
        customerRepository.delete(savedCustomer);

        Optional<Customer> customerOptional =
            assertDoesNotThrow(() -> customerRepository.findById(savedCustomer.getId()));

        //then
        assertTrue(customerOptional.isEmpty());
    }

}