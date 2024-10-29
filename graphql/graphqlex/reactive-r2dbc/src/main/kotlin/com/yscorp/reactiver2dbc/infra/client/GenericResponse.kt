package com.yscorp.reactiver2dbc.infra.client

import org.springframework.graphql.ResponseError

data class GenericResponse<T>(
    val data: T? = null,
    val errors: List<ResponseError> = emptyList()
) {
    val dataPresent: Boolean
        get() = data != null

    constructor(data: T) : this(data, emptyList())

    constructor(errors: List<ResponseError>) : this(null, errors)

}
