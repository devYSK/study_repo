package com.fastcampus.ecommerce.admin.controller;

import com.fastcampus.ecommerce.admin.infrastructure.adapter.ProductAdapter;
import com.fastcampus.ecommerce.admin.service.dto.ProductDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductGraphqlController {

    private final ProductAdapter productAdapter;

    @GetMapping(value = "/products/list")
    public List<ProductDTO> list(@PageableDefault Pageable pageable) {
        return productAdapter.findAll(pageable);
    }
}
