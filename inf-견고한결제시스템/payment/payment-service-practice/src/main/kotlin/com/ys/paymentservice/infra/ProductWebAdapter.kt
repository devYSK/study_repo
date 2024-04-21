package com.ys.paymentservice.infra

import com.ys.paymentservice.common.WebAdapter
import com.ys.paymentservice.domain.product.Product
import reactor.core.publisher.Flux

@WebAdapter
class ProductWebAdapter (
  private val productClient: ProductClient
) {

  fun getProducts(cartId: Long, productIds: List<Long>): Flux<Product> {
    return productClient.getProducts(cartId, productIds)
  }

}