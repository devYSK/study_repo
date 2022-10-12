package com.lannstark.lec01

import java.lang.IllegalArgumentException

fun main() {

    val person = Person("영수르");

    startsWith(person.name)

    val number1: Int? = 3
    val number2: Long = number1?.toLong() ?: 0L

    """
       ABCD
       EFG
       ${number1}
    """.trimIndent()
}




fun startsWith(str: String) : Boolean {
    return str.startsWith("A")
}

