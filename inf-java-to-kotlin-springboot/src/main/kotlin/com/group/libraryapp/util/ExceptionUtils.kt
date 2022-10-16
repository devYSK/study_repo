package com.group.libraryapp.util

fun fail(): Nothing { // Nothing 타입은 이 함수는 항상 정상적으로 종료되지 않는다는 의미이다.
    throw IllegalArgumentException()
}