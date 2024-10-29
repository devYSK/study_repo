package com.yscorp.reactiver2dbc

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ReactiveR2dbcApplication

fun main(args: Array<String>) {
    runApplication<ReactiveR2dbcApplication>(*args)
}
