package com.example.paymentservice2.payment.adapter.out.web.product.client

import com.example.paymentservice2.payment.domain.Product
import reactor.core.publisher.Flux

interface ProductClient {

  fun getProducts(cartId: Long, productIds: List<Long>): Flux<Product>
}