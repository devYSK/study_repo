package com.yscorp.reactivemongo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ReactiveMongoApplication

fun main(args: Array<String>) {
    runApplication<ReactiveMongoApplication>(*args)
}
