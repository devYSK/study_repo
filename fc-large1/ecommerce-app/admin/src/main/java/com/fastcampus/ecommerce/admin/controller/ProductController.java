package com.fastcampus.ecommerce.admin.controller;

import com.fastcampus.ecommerce.admin.domain.product.ProductDetailView;
import com.fastcampus.ecommerce.admin.exception.ProductNotFoundException;
import com.fastcampus.ecommerce.admin.infrastructure.adapter.ProductAdapter;
import com.fastcampus.ecommerce.admin.service.ProductService;
import com.fastcampus.ecommerce.admin.service.dto.ProductDTO;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private static final String MENU_KEY = "products";

    private final ProductService productService;
    private final ProductAdapter productAdapter;

    @GetMapping(value = {"/products", "/products/"})
    public String list(Model model) {
        // List<ProductDTO> productDTOS = productService.findAll();
        final List<ProductDTO> all = productAdapter.findAll(PageRequest.of(0, 10));
        model.addAttribute("products", all);
        model.addAttribute("menuId", MENU_KEY);
        return "/products/products";
    }

    @GetMapping("/products/product-detail")
    public String detail(@RequestParam Long productId, Model model) {
        Optional<ProductDetailView> optionalProductDetailView = productService.getProductDetail(productId);
        ProductDetailView productDetailView = optionalProductDetailView.orElseThrow(() -> new ProductNotFoundException("Not found product info"));
        model.addAttribute("productDetail", productDetailView);
        model.addAttribute("menuId", MENU_KEY);
        return "/products/product-detail";
    }
}
