package com.yscorp.reactiver2dbc.interfaces.graphql

import com.yscorp.reactiver2dbc.domain.Address
import com.yscorp.reactiver2dbc.domain.Customer
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller
import reactor.core.publisher.Mono

@Controller
class AddressController {

    @SchemaMapping
    suspend fun address(customer: Customer): Address {
        return Address(customer.name + " street", customer.name + " city")

    }
}