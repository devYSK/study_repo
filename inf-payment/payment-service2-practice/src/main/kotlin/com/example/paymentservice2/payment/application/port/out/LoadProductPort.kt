package com.example.paymentservice2.payment.application.port.out

import com.example.paymentservice2.payment.domain.Product
import reactor.core.publisher.Flux

interface LoadProductPort {

  fun getProducts(cartId: Long, productIds: List<Long>): Flux<Product>
}