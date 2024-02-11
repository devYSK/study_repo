package com.fastcampus.ecommerce.admin.repository;

import com.fastcampus.ecommerce.admin.domain.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findTop100ByIsDeletedIsFalse();
}
