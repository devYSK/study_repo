package com.fastcampus.ecommerce.admin.infrastructure.adapter;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
class ProductGraphqlConnection {
    @JsonProperty("products")
    List<ProductGraphqlDTO> productGraphqlDTOS;
}
