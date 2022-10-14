package com.lannstark.lec12

/**
 *
 * @author   : ysk
 */
class Person(
    val name: String,
    val age: Int,
) {

    companion object Factory : Log{
        private const val MIN_AGE = 1
        fun newBaby(name: String): Person {
            return Person(name, MIN_AGE)
        }

        override fun log() {
            println("나는 Person 클래스의 companion object이다.")
        }
    }
}

interface Log {
    fun log()
}

fun main() {

    moveSomething(object : Movable{
        override fun move() {
            println("move")
        }

        override fun fly() {
            println("fly")
        }
    })

}

private fun moveSomething(movable: Movable) {
    movable.move()
    movable.fly()
}