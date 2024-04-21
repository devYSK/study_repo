package com.ys.paymentservice.infra

import com.ys.paymentservice.domain.product.Product
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux

@Component
class MockProductClient : ProductClient {

    override fun getProducts(cartId: Long, productIds: List<Long>): Flux<Product> {
        return Flux.fromIterable(
            productIds.map { productId ->
                Product(
                    id = productId,
                    amount = productId * 10000,
                    quantity = 2,
                    name = "test_product_$productId",
                    sellerId = 1
                )
            }
        )
    }

}