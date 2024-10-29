package com.yscorp.reactiver2dbc.infra.client

data class CustomerNotFound(
    var id: Int,
    val message: String = "user not found",
    ) : CustomerResponse {
}