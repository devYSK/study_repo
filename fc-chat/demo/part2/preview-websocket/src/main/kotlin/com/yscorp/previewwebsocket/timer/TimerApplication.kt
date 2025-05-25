package com.yscorp.previewwebsocket.timer

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class TimerApplication {

}

fun main(args: Array<String>) {
    SpringApplication.run(TimerApplication::class.java, *args)
}
