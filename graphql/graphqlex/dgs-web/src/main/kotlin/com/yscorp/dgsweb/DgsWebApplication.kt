package com.yscorp.dgsweb

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DgsWebApplication

fun main(args: Array<String>) {
	runApplication<DgsWebApplication>(*args)
}
