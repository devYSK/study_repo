package com.yscorp.reactiver2dbc.interfaces.graphql

import com.yscorp.reactiver2dbc.dto.AllTypes
import com.yscorp.reactiver2dbc.dto.Car
import com.yscorp.reactiver2dbc.dto.Product
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.math.BigDecimal
import java.math.BigInteger
import java.time.LocalDate
import java.time.LocalTime
import java.time.OffsetDateTime
import java.util.*
import java.util.Map

@Controller
class AllTypeController {
    private val allTypes: AllTypes = AllTypes(
        UUID.randomUUID(),
        10,
        10.12f,
        "atlanta",
        false,
        120000000000L,
        "12".toByte(),
        "100".toShort(),
        BigDecimal.valueOf(123456789.123456789),
        BigInteger.valueOf(1234567890),
        LocalDate.now(),
        LocalTime.now(),
        OffsetDateTime.now(),
        Car.HONDA
    )

    @QueryMapping
    fun get(): Mono<AllTypes> {
        return Mono.just<AllTypes>(allTypes)
    }

    @QueryMapping
    fun products(): Flux<Product> {
        return Flux.just(
            Product(
                "banana", Map.of(
                    "expiry date", "01/01/2025",
                    "color", "yellow"
                )
            ),
            Product(
                "mac", Map.of(
                    "cpu", "8",
                    "RAM", "32g"
                )
            )
        )
    }
}