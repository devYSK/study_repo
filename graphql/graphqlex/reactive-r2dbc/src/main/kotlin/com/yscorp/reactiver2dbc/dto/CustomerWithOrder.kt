package com.yscorp.reactiver2dbc.dto

data class CustomerWithOrder(
    var id: Int,

    val name: String,

    val age: Int,

    val city: String,

    val orders: MutableList<CustomerOrderDto>,
) {
}