package com.yscorp.reactiver2dbc.interfaces.exception

import com.yscorp.reactiver2dbc.dto.CustomerDto
import org.springframework.graphql.execution.ErrorType
import reactor.core.publisher.Mono
import java.util.Map

object ApplicationErrors {
    fun <T> noSuchUser(id: Int?): Mono<T> {
        return Mono.error(
            ApplicationException(
                ErrorType.BAD_REQUEST, "No such user", Map.of(
                    "customerId", id
                )
            )
        )
    }

    fun <T> mustBe18(dto: CustomerDto?): Mono<T> {
        return Mono.error(
            ApplicationException(
                ErrorType.BAD_REQUEST, "Must be 18 or above", Map.of(
                    "input", dto
                )
            )
        )
    }
}
