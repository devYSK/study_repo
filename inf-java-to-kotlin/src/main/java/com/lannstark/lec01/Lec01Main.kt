package com.lannstark.lec01

import java.lang.IllegalArgumentException

fun main() {

    val person = Person("영수르");

    startsWith(person.name)
}




fun startsWith(str: String) : Boolean {
    return str.startsWith("A")
}

