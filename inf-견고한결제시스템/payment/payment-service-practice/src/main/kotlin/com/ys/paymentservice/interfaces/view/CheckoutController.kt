package com.ys.paymentservice.interfaces.view

import com.ys.paymentservice.common.IdempotencyCreator
import com.ys.paymentservice.common.WebAdapter
import com.ys.paymentservice.interfaces.request.CheckoutRequest
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import reactor.core.publisher.Mono

@WebAdapter
@Controller
class CheckoutController(
    private val checkoutService: com.ys.paymentservice.application.CheckoutService
) {

    @GetMapping("/")
    fun checkoutPage(checkoutRequest: CheckoutRequest, model: Model): Mono<String> {

        val checkoutCommand = com.ys.paymentservice.application.CheckoutCommand(
            cartId = checkoutRequest.cartId,
            buyerId = checkoutRequest.buyerId,
            productIds = checkoutRequest.productIds,
            idempotencyKey = IdempotencyCreator.create(checkoutRequest.seed)
        )

        checkoutService.checkout(checkoutCommand)
            .map {
                model.addAttribute("orderId", it.orderId)
                model.addAttribute("orderName", it.orderName)
                model.addAttribute("amount", it.amount)
                "checkout"
            }

        return Mono.just("checkout")
    }
}