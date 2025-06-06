package com.example.domain


import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.ColumnType
import org.jetbrains.exposed.v1.core.Table

class EnumListColumnType<T : Enum<T>>(
    private val enumClass: Class<T>,
    private val varcharLength: Int,
) : ColumnType<List<T>>() {
    override fun sqlType() = "VARCHAR(${varcharLength})"

    override fun valueFromDB(value: Any): List<T> {
        if (value is String) {
            return value.split(',').map { java.lang.Enum.valueOf(enumClass, it) }
        }
        throw IllegalArgumentException("Unexpected value of type String expected but ${value::class} found.")
    }

    override fun notNullValueToDB(value: List<T>): Any {
        return value.joinToString(",") { it.toString() }
    }
}

fun <T : Enum<T>> Table.enumList(name: String, enumClass: Class<T>, varcharLength: Int): Column<List<T>> =
    registerColumn(name, EnumListColumnType(enumClass, varcharLength))
