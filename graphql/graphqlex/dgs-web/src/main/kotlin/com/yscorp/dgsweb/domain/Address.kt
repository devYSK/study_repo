package com.yscorp.dgsweb.domain

import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "addresses")
data class Address(
    @Id
    @GeneratedValue
    var uuid: UUID ,

    var street: String ,

    var city: String ,

    var zipcode: String
)
