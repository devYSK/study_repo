package com.fastcampus.helloecommerceservice.controller.category;

import com.fastcampus.helloecommerceservice.controller.cart.CartModelAttributeKeys;
import com.fastcampus.helloecommerceservice.controller.dto.product.CategoryProductDTO;
import com.fastcampus.helloecommerceservice.domain.customer.CustomerDetail;
import com.fastcampus.helloecommerceservice.service.cart.CartService;
import com.fastcampus.helloecommerceservice.service.category.CategoryProductService;
import com.fastcampus.helloecommerceservice.service.category.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Objects;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CategoryProductController {

    private final CategoryProductService categoryProductService;
    private final CategoryService categoryService;
    private final CartService cartService;
    private static final int ITEM_PER_ROW = 3;

    @GetMapping(value = "/category/products")
    public String list(
            @AuthenticationPrincipal CustomerDetail customerDetail,
            @RequestParam(value = "categoryId") Long categoryId,
            @PageableDefault Pageable pageable,
            Model model
    ) {
        log.info(">>> Category별 상품 조회, {}", categoryId);

        // 장바구니 개수
        if (Objects.nonNull(customerDetail)) {
            int countCartProduct = cartService.countCartProduct(customerDetail.getCustomer().getCustomerId());
            model.addAttribute(CartModelAttributeKeys.CART_ITEM_COUNT_KEY, countCartProduct);
        }

        List<CategoryProductDTO> categoryProducts = this.categoryProductService.findAllBy(categoryId, pageable);
        List<List<CategoryProductDTO>> categoryProductPartition = ListUtils.partition(categoryProducts, ITEM_PER_ROW);

        String categoryName = this.categoryService.getName(categoryId);

        model.addAttribute("categoryId", categoryId);
        model.addAttribute("categoryName", categoryName);
        model.addAttribute("categoryProductPartition", categoryProductPartition);
        log.info(">>> Category별 상품 조회 결과, {}", categoryProducts);

        return "/category/products";
    }
}
