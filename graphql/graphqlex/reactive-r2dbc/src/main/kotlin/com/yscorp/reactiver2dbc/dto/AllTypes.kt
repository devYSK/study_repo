package com.yscorp.reactiver2dbc.dto

import java.math.BigDecimal
import java.math.BigInteger
import java.time.LocalDate
import java.time.LocalTime
import java.time.OffsetDateTime
import java.util.*

data class AllTypes(
    val id: UUID,
    val height: Int?,
    val temperature: Float?,
    val city: String?,
    val isValid: Boolean?,
    val distance: Long?,
    val ageInYears: Byte?,
    val ageInMonths: Short?,
    val bigDecimal: BigDecimal?,
    val bigInteger: BigInteger?,
    val date: LocalDate?,
    val time: LocalTime?,
    val dateTime: OffsetDateTime?,
    val car: Car?,
)

enum class Car {
    BMW,
    HONDA
}
