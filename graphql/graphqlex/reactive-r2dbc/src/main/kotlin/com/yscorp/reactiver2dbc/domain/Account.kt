package com.yscorp.reactiver2dbc.domain

import java.util.UUID

class Account(
    val id: UUID,
    val amount: Int,
    val accountType: String,
) {
}