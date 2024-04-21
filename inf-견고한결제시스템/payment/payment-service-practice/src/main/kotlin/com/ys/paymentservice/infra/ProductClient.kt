package com.ys.paymentservice.infra

import com.ys.paymentservice.domain.product.Product
import com.ys.paymentservice.payment.domain.product.Product
import reactor.core.publisher.Flux

interface ProductClient {

  fun getProducts(cartId: Long, productIds: List<Long>): Flux<Product>
}