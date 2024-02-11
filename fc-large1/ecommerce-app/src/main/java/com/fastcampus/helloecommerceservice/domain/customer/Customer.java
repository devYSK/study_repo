package com.fastcampus.helloecommerceservice.domain.customer;

import com.fastcampus.helloecommerceservice.controller.dto.customer.CustomerRegisterDTO;
import com.fastcampus.helloecommerceservice.enums.CustomerPermission;
import com.fastcampus.helloecommerceservice.enums.ECommerceRole;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "customers", schema = "ecommerce")
@Data
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long customerId;
    @Column(name = "name")
    private String name;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "email")
    private String email;
    @Column(name = "age")
    private int age;
    @Column(name = "password")
    private String password;
    @Column(name = "address")
    private String address;
    @Column(name = "role")
    @Enumerated(value = EnumType.STRING)
    private ECommerceRole role;
    @Column(name = "permission")
    @Enumerated(value = EnumType.STRING)
    private CustomerPermission permission;
    @Column(name = "is_deleted")
    private boolean isDeleted = false;
    @Column(name = "is_activated")
    private boolean isActivated = true;
    @Column(name = "created_at")
    private OffsetDateTime createdAt = OffsetDateTime.now();
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt = OffsetDateTime.now();

    public static Customer createGeneralCustomer(CustomerRegisterDTO customerRegisterDTO, PasswordEncoder passwordEncoder) {

        Customer customer = new Customer();
        customer.phoneNumber = customerRegisterDTO.getPhoneNumber();
        customer.email = customerRegisterDTO.getEmail();
        customer.name = customerRegisterDTO.getUsername();
        customer.address = customerRegisterDTO.getAddress();
        customer.age = customerRegisterDTO.getAge();
        customer.permission = CustomerPermission.GENERAL;
        customer.role = ECommerceRole.CUSTOMER;
        customer.password = passwordEncoder.encode(customerRegisterDTO.getPassword1());

        return customer;
    }
}
