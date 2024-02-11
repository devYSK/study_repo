package com.fastcampus.helloecommerceservice.controller;

import com.fastcampus.helloecommerceservice.controller.cart.CartModelAttributeKeys;
import com.fastcampus.helloecommerceservice.controller.dto.home.HomeDisplayDTO;
import com.fastcampus.helloecommerceservice.domain.customer.CustomerDetail;
import com.fastcampus.helloecommerceservice.service.HomeDisplayService;
import com.fastcampus.helloecommerceservice.service.cart.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Objects;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final HomeDisplayService homeDisplayService;
    private final CartService cartService;

    @GetMapping(value = {"/", "/index", ""})
    public String index(@AuthenticationPrincipal CustomerDetail customerDetail, Model model) {

        log.info(">>> Login Customer, {}", customerDetail);
        model.addAttribute("customerDetail", customerDetail);
        if (Objects.nonNull(customerDetail)) {
            int countCartProduct = cartService.countCartProduct(customerDetail.getCustomer().getCustomerId());
            model.addAttribute(CartModelAttributeKeys.CART_ITEM_COUNT_KEY, countCartProduct);
        }

        HomeDisplayDTO homeDisplayDTO = homeDisplayService.displayHome();

        model.addAttribute("main_image_banner", homeDisplayDTO.getImageBannerSrc());
        model.addAttribute("recommend_product_partitions", homeDisplayDTO.getRecommendProductDTOs());

        log.info("Main Image Banner={}, Recommend Products={}", homeDisplayDTO.getImageBannerSrc(), homeDisplayDTO.getRecommendProductDTOs());

        return "/index";
    }
}
