package com.yscorp.ex1.domain

data class Client(
    val id: Long,
    val accountId: String,
    val firstName: String,
    val middleName: String,
    val lastName: String,
)