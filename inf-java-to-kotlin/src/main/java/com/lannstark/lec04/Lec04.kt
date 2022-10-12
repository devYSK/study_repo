package com.lannstark.lec04


fun main() {
    val money1 = Money(1000L)
    val money2 = Money(2000L)

    println(money1 + money2);
}


fun startsWith(obj: Any) : Boolean {

    for (i in 1..3)

    return when(obj) {
        is String -> obj.startsWith("A")
        else -> false
    }
}

