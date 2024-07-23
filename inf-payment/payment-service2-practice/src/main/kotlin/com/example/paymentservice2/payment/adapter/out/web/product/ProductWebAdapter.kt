package com.example.paymentservice2.payment.adapter.out.web.product

import com.example.paymentservice2.common.WebAdapter
import com.example.paymentservice2.payment.adapter.out.web.product.client.ProductClient
import com.example.paymentservice2.payment.application.port.out.LoadProductPort
import com.example.paymentservice2.payment.domain.Product
import reactor.core.publisher.Flux

@WebAdapter
class ProductWebAdapter (
  private val productClient: ProductClient
) : LoadProductPort {

  override fun getProducts(cartId: Long, productIds: List<Long>): Flux<Product> {
    return productClient.getProducts(cartId, productIds)
  }
}