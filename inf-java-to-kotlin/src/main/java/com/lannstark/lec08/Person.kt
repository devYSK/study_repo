package com.lannstark.lec08

class Person (
        val name: String,
        var age: Int
        ) {

    constructor(name: String) : this(name, 1) {
        if (name.equals("hi")) {
            println()
        }
    }


    val isAdult: Boolean get() = this.age >= 20

    fun isAdult(): Boolean {
        return this.age >= 20
    }


}