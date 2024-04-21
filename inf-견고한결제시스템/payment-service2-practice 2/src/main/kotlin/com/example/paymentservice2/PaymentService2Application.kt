package com.example.paymentservice2

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
class PaymentService2Application

fun main(args: Array<String>) {
  runApplication<PaymentService2Application>(*args)
}
