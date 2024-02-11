package com.fastcamedu.hellographql.customer;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@DgsComponent
public class CustomerDataFetcher {

    private List<Customer> customers = new ArrayList<>(Arrays.asList(
            new Customer("김철수", 32),
            new Customer("이창수", 22),
            new Customer("강현만", 12),
            new Customer("조동현", 42),
            new Customer("진상민", 52)
    ));

    @DgsQuery
    public List<Customer> customers(@InputArgument String nameFilter) {
        return customers.stream().filter(c -> c.getName().contains(nameFilter)).collect(Collectors.toList());
    }

    @DgsMutation
    public List<Customer> addCustomer(@InputArgument CustomerInput customerInput) {
        customers.add(new Customer(customerInput.getName(), customerInput.getAge()));
        return customers;
    }
}
