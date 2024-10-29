package com.yscorp.reactiver2dbc.dto

data class DeleteResponseDto(
    val id: Int,
    val status: Status,
) {
}

enum class Status {
    SUCCESS,
    FAILURE
}
