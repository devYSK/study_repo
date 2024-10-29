package com.yscorp.ex1.domain

class AuthPayload(
    var token: String,
    val user: User,
) {
}