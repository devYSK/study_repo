package com.yscorp.reactiver2dbc.interfaces.graphql

import com.yscorp.reactiver2dbc.domain.Account
import com.yscorp.reactiver2dbc.domain.Customer
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller
import reactor.core.publisher.Mono
import java.util.*
import java.util.concurrent.ThreadLocalRandom

@Controller
class AccountController {

    @SchemaMapping
    fun account(customer: Customer): Mono<Account> {
        val type = if (ThreadLocalRandom.current().nextBoolean()) "CHECKING" else "SAVING"

        return Mono.just(
            Account(
                UUID.randomUUID(),
                ThreadLocalRandom.current().nextInt(1, 1000),
                type
            )
        )
    }

}