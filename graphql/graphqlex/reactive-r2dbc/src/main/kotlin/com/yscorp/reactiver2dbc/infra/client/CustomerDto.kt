package com.yscorp.reactiver2dbc.infra.client

data class CustomerDto (
    val id: Int = 0,
    val name: String,
    val age: Int,
    val city: String,
) : CustomerResponse {

}
