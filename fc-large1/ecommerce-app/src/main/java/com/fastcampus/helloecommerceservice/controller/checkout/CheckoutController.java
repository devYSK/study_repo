package com.fastcampus.helloecommerceservice.controller.checkout;

import com.fastcampus.helloecommerceservice.controller.dto.customer.CustomerDTO;
import com.fastcampus.helloecommerceservice.controller.dto.product.ProductDTO;
import com.fastcampus.helloecommerceservice.domain.cart.Cart;
import com.fastcampus.helloecommerceservice.domain.cart.CartItemProduct;
import com.fastcampus.helloecommerceservice.domain.customer.Customer;
import com.fastcampus.helloecommerceservice.domain.customer.CustomerDetail;
import com.fastcampus.helloecommerceservice.domain.product.Product;
import com.fastcampus.helloecommerceservice.exception.NotFoundCartException;
import com.fastcampus.helloecommerceservice.exception.NotFoundCartItemException;
import com.fastcampus.helloecommerceservice.exception.NotFoundCustomerException;
import com.fastcampus.helloecommerceservice.exception.NotFoundProductException;
import com.fastcampus.helloecommerceservice.service.cart.CartService;
import com.fastcampus.helloecommerceservice.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CheckoutController {

    public static final String ATTRIBUTE_NAME_CART_ID = "cartId";
    private final CartService cartService;
    private final ProductService productService;

    @GetMapping(value = "/checkout/direct-checkout")
    public String directCheckout(@AuthenticationPrincipal CustomerDetail customerDetail, @RequestParam("productId") Long productId, Model model) {
        // Direct Purchase
        log.info(">>> Handle payment for customer, {}", customerDetail);
        if (Objects.isNull(customerDetail)) {
            throw new NotFoundCustomerException("고객 정보를 찾을 수 없습니다.");
        }
        Customer customer = customerDetail.getCustomer();

        Optional<Product> optionalProduct = this.productService.findById(productId);
        if (optionalProduct.isEmpty()) {
            throw new NotFoundProductException("해당 상품 정보가 없습니다.");
        }
        Product product = optionalProduct.get();

        model.addAttribute("product", ProductDTO.of(product));
        model.addAttribute("customer", CustomerDTO.of(customer));
        model.addAttribute("totalAmount", product.getPrice());

        return "/checkout/direct-checkout";
    }

    @GetMapping(value = "/checkout/order-checkout")
    public String orderCheckout(@AuthenticationPrincipal CustomerDetail customerDetail, @RequestParam("cartId") Long cartId, Model model) {
        // Cart Purchase
        log.info(">>> Handle payment for customer, {}", customerDetail);
        if (Objects.isNull(customerDetail)) {
            throw new NotFoundCustomerException("Not found customer info");
        }

        Customer customer = customerDetail.getCustomer();
        Optional<Cart> optionalCart = this.cartService.getCartById(cartId);
        if (optionalCart.isEmpty()) {
            throw new NotFoundCartException("고객의 장바구니 정보를 얻을 수 없습니다. 다시 시도하시기 바랍니다.");
        }
        List<CartItemProduct> cartItemProducts = this.cartService.getCartItems(customerDetail.getCustomer().getCustomerId());
        if (cartItemProducts.isEmpty()) {
            throw new NotFoundCartItemException("Not found cart items for purchase");
        }
        int totalAmount = cartItemProducts.stream().mapToInt(c -> c.getProductPrice().intValue()).sum();

        model.addAttribute("cartItemProducts", cartItemProducts);
        model.addAttribute(ATTRIBUTE_NAME_CART_ID, cartId);
        model.addAttribute("customer", CustomerDTO.of(customer));
        model.addAttribute("totalAmount", totalAmount);

        return "/checkout/order-checkout";
    }
}
