package com.fastcampus.ecommerce.admin.controller;

import com.fastcampus.ecommerce.admin.domain.customer.Customer;
import com.fastcampus.ecommerce.admin.exception.NotFoundCustomerException;
import com.fastcampus.ecommerce.admin.service.CustomerService;
import com.fastcampus.ecommerce.admin.service.dto.CustomerDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@AllArgsConstructor
public class CustomerController {
    private final CustomerService customerService;
    private static final String ATTRIBUTE_KEY_CUSTOMERS = "customers";
    private static final String MENU_KEY = "customers";

    @GetMapping(value = {"/customers", "/customers/"})
    public String index(Model model) {
        List<Customer> customers = customerService.findTop100ByActiveCustomer();
        List<CustomerDTO> customerDTOs = customers.stream()
                .map(customer -> CustomerDTO.of(
                        customer.getCustomerId(),
                        customer.getCustomerName(),
                        customer.getPhoneNumber(),
                        customer.getAddress(),
                        customer.getGrade(),
                        customer.getCreatedAt(),
                        customer.getUpdatedAt()
                ))
                .collect(Collectors.toList());
        model.addAttribute(ATTRIBUTE_KEY_CUSTOMERS, customerDTOs);
        model.addAttribute("menuId", MENU_KEY);
        return "/customers/customers";
    }

    @GetMapping("/customers/customer-detail")
    public String detail(@RequestParam Long customerId, Model model) {
        Customer customer = customerService.findById(customerId).orElseThrow(() -> new NotFoundCustomerException("고객 정보를 찾을 수 없습니다." + customerId));
        model.addAttribute("customer", customer);
        model.addAttribute("menuId", MENU_KEY);
        return "/customers/customer-detail";
    }
}
