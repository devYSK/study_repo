package com.fastcampus.helloecommerceservice.service;

import com.fastcampus.helloecommerceservice.controller.dto.home.HomeDisplayDTO;
import com.fastcampus.helloecommerceservice.controller.dto.home.RecommendProductDTO;
import com.fastcampus.helloecommerceservice.domain.marketing.Marketing;
import com.fastcampus.helloecommerceservice.enums.AdvertiseType;
import com.fastcampus.helloecommerceservice.enums.MarketingPlacement;
import com.fastcampus.helloecommerceservice.enums.MarketingType;
import com.fastcampus.helloecommerceservice.repository.marketing.MarketingRepository;
import com.fastcampus.helloecommerceservice.repository.product.ProductRepository;
import com.fastcampus.helloecommerceservice.service.product.RecommendProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class HomeDisplayService {
    public static final int ITEM_PER_ROW = 3;
    private final MarketingRepository marketingRepository;
    private final RecommendProductService recommendProductService;

    public HomeDisplayDTO displayHome() {
        // 온사이트 마케팅 배너
        Optional<Marketing> marketingOptional = marketingRepository.findFirstByMarketingTypeAndMarketingPlacementAndAdvertiseType(
                MarketingType.ON_SITE, MarketingPlacement.HOME_BANNER, AdvertiseType.BANNER
        );

        String mainImageBannerSrc = Strings.EMPTY;
        if (marketingOptional.isPresent()) {
            mainImageBannerSrc = marketingOptional.get().getAdvertiseValue();
        }
        log.debug("Main Image Banner: {}", mainImageBannerSrc);

        // 추천 상품 목록
        List<RecommendProductDTO> recommendProductDTOS = recommendProductService.recommend();
        List<List<RecommendProductDTO>> recommendProductPartition = ListUtils.partition(recommendProductDTOS, ITEM_PER_ROW);

        log.debug("추천 상품 목록 : {}", recommendProductPartition);

        return HomeDisplayDTO.of(mainImageBannerSrc, recommendProductPartition);
    }
}
