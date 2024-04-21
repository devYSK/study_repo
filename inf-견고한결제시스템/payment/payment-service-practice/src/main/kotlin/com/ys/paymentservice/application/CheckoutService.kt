package com.ys.paymentservice.application

import com.ys.paymentservice.common.UseCase
import com.ys.paymentservice.domain.CheckoutResult
import com.ys.paymentservice.domain.payment.PaymentEvent
import com.ys.paymentservice.domain.payment.PaymentOrder
import com.ys.paymentservice.domain.payment.PaymentPersistentAdapter
import com.ys.paymentservice.domain.payment.PaymentStatus
import com.ys.paymentservice.domain.product.Product
import com.ys.paymentservice.infra.ProductWebAdapter
import reactor.core.publisher.Mono

@UseCase
class CheckoutService (
    private val loadProductPort: ProductWebAdapter,
    private val savePaymentPort: PaymentPersistentAdapter
) {

    fun checkout(command: com.ys.paymentservice.application.CheckoutCommand): Mono<CheckoutResult> {
        return loadProductPort.getProducts(command.cartId, command.productIds)
            .collectList()
            .map { createPaymentEvent(command, it) }
            .flatMap { savePaymentPort.save(it).thenReturn(it)  }
            .map { CheckoutResult(amount = it.totalAmount(), orderId = it.orderId, orderName = it.orderName) }
    }

    private fun createPaymentEvent(command: com.ys.paymentservice.application.CheckoutCommand, products: List<Product>): PaymentEvent {
        return PaymentEvent(
            buyerId = command.buyerId,
            orderId = command.idempotencyKey,
            orderName = products.joinToString { it.name },
            paymentOrders = products.map {
                PaymentOrder(
                    sellerId = it.sellerId,
                    orderId = command.idempotencyKey,
                    productId = it.id,
                    amount = it.amount,
                    paymentStatus = PaymentStatus.NOT_STARTED,
                )
            }
        )
    }
}