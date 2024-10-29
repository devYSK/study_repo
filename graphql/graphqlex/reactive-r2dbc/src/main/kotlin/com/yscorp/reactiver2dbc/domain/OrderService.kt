package com.yscorp.reactiver2dbc.domain

import com.yscorp.reactiver2dbc.dto.CustomerOrderDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.util.function.Tuple2
import reactor.util.function.Tuples
import java.util.*
import java.util.function.Function

@Service
class OrderService {
    private val map: Map<String, List<CustomerOrderDto>> = java.util.Map.of(
        "sam", listOf(
            CustomerOrderDto(UUID.randomUUID(), "sam-product-1"),
            CustomerOrderDto(UUID.randomUUID(), "sam-product-2")
        ),
        "mike", listOf(
            CustomerOrderDto(UUID.randomUUID(), "mike-product-1"),
            CustomerOrderDto(UUID.randomUUID(), "mike-product-2"),
            CustomerOrderDto(UUID.randomUUID(), "mike-product-3")
        )
    )

    fun ordersByCustomerName(name: String): Flow<CustomerOrderDto> {
        return Flux.fromIterable(map.getOrDefault(name, emptyList<CustomerOrderDto>())).asFlow()
    }


    suspend fun fetchOrdersAsMap(customers: List<Customer>): Map<Customer, List<CustomerOrderDto>> {
        return Flux.fromIterable(customers)
            .map { c -> Tuples.of(c, map.getOrDefault(c.name, emptyList())) }
            .collectMap(
                { it.t1 },
                { it.t2 }
            ).awaitSingle()
    }
}
