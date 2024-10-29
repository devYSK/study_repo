package com.yscorp.reactiver2dbc.dto

data class CustomerEvent(
    val id: Int,
    val action: Action,
) {
}
enum class Action {
    CREATED,
    UPDATED,
    DELETED
}
