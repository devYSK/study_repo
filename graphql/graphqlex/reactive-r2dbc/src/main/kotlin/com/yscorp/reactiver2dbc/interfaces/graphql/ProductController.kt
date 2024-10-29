package com.yscorp.reactiver2dbc.interfaces.graphql

import graphql.schema.TypeResolver
import graphql.schema.idl.RuntimeWiring
import graphql.schema.idl.TypeRuntimeWiring
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.execution.ClassNameTypeResolver
import org.springframework.graphql.execution.RuntimeWiringConfigurer
import org.springframework.stereotype.Controller
import reactor.core.publisher.Flux
import java.time.LocalDate
import java.util.*

// for graphql interface
// TypeResolverConfig가 필요하다. @see ScalarConfig
@Controller
class ProductController {

    @QueryMapping
    fun iProducts(): Flux<Any> {
        return Flux.just(
            FruitDto(description = "banana", price = 1, expiryDate = LocalDate.now().plusDays(3)),
            FruitDto(description = "apple", price = 2, expiryDate = LocalDate.now().plusDays(5)),
            Electronics(description = "mac book", price = 600, brand = "APPLE"),
            Electronics(description = "phone", price = 400, brand = "SAMSUNG"),
            MyBook(description = "java", price = 40, author = "venkat")
        )
    }
}

data class FruitDto(
    val id: UUID? = UUID.randomUUID(),

    val description: String,

    val price: Int,

    val expiryDate: LocalDate,
)

data class Electronics(
    val id: UUID? = UUID.randomUUID(),

    val description: String,

    val price: Int,

    val brand: String,
)

data class MyBook(
    val id: UUID? = UUID.randomUUID(),

    val description: String,

    val price: Int,

    val author: String,
)