package com.fastcampus.helloecommerceservice.service.customer;

import com.fastcampus.helloecommerceservice.domain.customer.Customer;
import com.fastcampus.helloecommerceservice.exception.DuplicatedEmailException;
import com.fastcampus.helloecommerceservice.exception.NotFoundCustomerException;
import com.fastcampus.helloecommerceservice.repository.customer.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    public void validateDuplicatedCustomer(String email) {
        Optional<Customer> optionalCustomer = Optional.ofNullable(customerRepository.findByEmail(email));
        if (optionalCustomer.isPresent()) {
            throw new DuplicatedEmailException("중복 이메일입니다.");
        }
    }
}
