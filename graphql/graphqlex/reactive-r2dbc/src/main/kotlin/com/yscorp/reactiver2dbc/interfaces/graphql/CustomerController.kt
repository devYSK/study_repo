package com.yscorp.reactiver2dbc.interfaces.graphql

import com.yscorp.reactiver2dbc.domain.*
import com.yscorp.reactiver2dbc.dto.CustomerDto
import com.yscorp.reactiver2dbc.dto.CustomerOrderDto
import com.yscorp.reactiver2dbc.dto.DeleteResponseDto
import com.yscorp.reactiver2dbc.logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import org.springframework.graphql.data.method.annotation.*
import org.springframework.stereotype.Controller
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Controller
class CustomerController(
    private val service: CustomerService,
    private val orderService: OrderService,

    ) {

    private val log = logger()

    // @QueryMapping
//    @SchemaMapping(typeName = "Query")
//    fun customers(): Flow<Customer> {
//        return service.allCustomers()
//    }

//    @SchemaMapping(typeName = "Customer")
//    fun orders(customer: Customer, @Argument limit: Int): Flow<CustomerOrderDto> {
//        log.info { "Orders method invoked for " + customer.name }
//        return orderService.ordersByCustomerName(customer.name)
//            .take(limit)
//    }

    @QueryMapping(value = "customers")
    fun customers(): Flow<Customer> {
        log.info { "Customers method invoked" }
        return service.allCustomers()
    }

    @QueryMapping(value = "tests")
    fun tests(): Flow<String> {
        log.info { "Tests method invoked" }
        return Flux.just("test1", "test2").asFlow()
    }

    // N + 1
    //    @BatchMapping(typeName = "Customer")
    //    public Flux<List<CustomerOrderDto>> orders(List<Customer> list){
    //        System.out.println("Orders method invoked for " + list);
    //        return this.orderService.ordersByCustomerNames(
    //            list.stream().map(Customer::getName).collect(Collectors.toList())
    //        );
    //    }

    @BatchMapping(typeName = "Customer")
    suspend fun orders(list: List<Customer>): Map<Customer, List<CustomerOrderDto>> {
        log.info { "Orders method invoked for $list" }
        return orderService.fetchOrdersAsMap(list)
    }

    @QueryMapping
    suspend fun customerById(@Argument id: Int): CustomerDto? {
        return service.customerById(id)
    }

    @MutationMapping
    suspend fun createCustomer(@Argument customer: CustomerDto): CustomerDto {
        return service.createCustomer(customer)
    }

    @MutationMapping
    suspend fun updateCustomer(@Argument id: Int, @Argument("customer") dto: CustomerDto): CustomerDto {
        return service.updateCustomer(id, dto)
    }

    @MutationMapping
    suspend fun deleteCustomer(@Argument id: Int): DeleteResponseDto {
        return service.deleteCustomer(id)
    }


}
