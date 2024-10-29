package com.yscorp.reactiver2dbc

import com.yscorp.reactiver2dbc.domain.CustomerService
import com.yscorp.reactiver2dbc.dto.CustomerWithOrder
import com.yscorp.reactiver2dbc.domain.OrderService
import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import kotlinx.coroutines.flow.*
import org.springframework.stereotype.Service

@Service
class CustomerOrderDataFetcher(
    private val customerService: CustomerService,
    private val orderService: OrderService,
) : DataFetcher<Flow<CustomerWithOrder>> {

    @Throws(Exception::class)
    override fun get(environment: DataFetchingEnvironment): Flow<CustomerWithOrder> {
        val includeOrders = environment.selectionSet.contains("orders")
        println(includeOrders)
        return customerService.allCustomers()
            .map { customer ->
                CustomerWithOrder(
                    customer.id,
                    customer.name,
                    customer.age,
                    customer.city,
                    mutableListOf()
                )
            }
            .let { updateOrdersTransformer(includeOrders).invoke(it) }
    }

    private fun updateOrdersTransformer(includeOrders: Boolean): (Flow<CustomerWithOrder>) -> Flow<CustomerWithOrder> {
        return if (includeOrders) { flow ->
            flow.flatMapConcat { customer -> fetchOrders(customer) }
        } else { flow -> flow }
    }

    private fun fetchOrders(customerWithOrder: CustomerWithOrder): Flow<CustomerWithOrder> {
        return orderService.ordersByCustomerName(customerWithOrder.name)
            .onEach { orders -> customerWithOrder.orders.add(orders) }
            .map { customerWithOrder }
    }
}