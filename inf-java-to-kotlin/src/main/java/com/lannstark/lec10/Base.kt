package com.lannstark.lec10

open class Base (open val number: Int = 100) {

    init {
        println("Base Class")
        println(number)
    }
}


class Derived(
    override val number: Int
) : Base(number) {
    init {
        println("derived Class")
    }
}

fun main() {
    val test = Derived(100)
}