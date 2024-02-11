package com.fastcampus.helloecommerceservice.controller.dto.home;

import lombok.Data;

import java.util.List;

@Data(staticConstructor = "of")
public class HomeDisplayDTO {
    private final String imageBannerSrc;
    private final List<List<RecommendProductDTO>> recommendProductDTOs;
}
