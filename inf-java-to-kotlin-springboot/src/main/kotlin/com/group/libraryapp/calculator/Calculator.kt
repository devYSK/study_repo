package com.group.libraryapp.calculator

import java.lang.IllegalArgumentException


data class Calculator(
    private var _number: Int
) {

    companion object {
        val ERROR_MESSAGE = "0으로 나눌 수 없습니다."
    }

    fun add(operand: Int) {
        this._number += operand
    }

    fun minus(operand: Int) {
        this._number -= operand
    }

    fun multiply(operand: Int) {
        this._number *= operand
    }

    fun divide(operand: Int) {
        if (operand == 0) {
            throw IllegalArgumentException(ERROR_MESSAGE)
        }

        this._number /= operand
    }


    val number: Int
        get() = this._number

}