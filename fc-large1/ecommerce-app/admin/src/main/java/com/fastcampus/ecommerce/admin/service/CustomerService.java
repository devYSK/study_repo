package com.fastcampus.ecommerce.admin.service;

import com.fastcampus.ecommerce.admin.domain.customer.Customer;
import com.fastcampus.ecommerce.admin.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    public List<Customer> findTop100ByActiveCustomer() {
        return customerRepository.findTop100ByIsDeletedIsFalse();
    }

    public Optional<Customer> findById(Long customerId) {
        return customerRepository.findById(customerId);
    }
}
