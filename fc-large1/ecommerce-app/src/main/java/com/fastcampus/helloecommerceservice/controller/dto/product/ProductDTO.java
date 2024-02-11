package com.fastcampus.helloecommerceservice.controller.dto.product;

import com.fastcampus.helloecommerceservice.domain.product.Product;
import com.fastcampus.helloecommerceservice.enums.DeliveryType;
import com.fastcampus.helloecommerceservice.enums.ProductStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ProductDTO {
    private Long productId;
    private String productName;
    private BigDecimal price;
    private Long vendorId;
    private ProductStatus status;
    private String imageUrl;
    private String imageDetailUrl;

    private DeliveryType deliveryType;
    private String productDesc;

    public static ProductDTO of(Product product) {
        return ProductDTO.builder()
                .productId(product.getId())
                .productName(product.getProductName())
                .price(product.getPrice())
                .vendorId(product.getVendorId())
                .status(product.getStatus())
                .imageUrl(product.getImageUrl())
                .imageDetailUrl(product.getImageDetailUrl())
                .productDesc(product.getProductDesc())
                .deliveryType(product.getDeliveryType())
                .build();
    }
}
