package com.yscorp.reactiver2dbc.domain

import org.springframework.data.annotation.Id

class Customer(
    @Id
    val id: Int = 0,
    var name: String,
    var age: Int,
    var city: String,
    ) {

}
