package com.yscorp.reactiver2dbc.dto

import com.yscorp.reactiver2dbc.domain.Customer

data class CustomerDto(
    val id: Int?,
    val name: String,
    val age: Int,
    val city: String,

    ) {

    constructor(customer: Customer) : this(
        id = customer.id,
        name = customer.name,
        age = customer.age,
        city = customer.city
    )


    fun toEntity(): Customer {
        return Customer(
            name = this.name,
            age = this.age,
            city = this.city
        )
    }
}
