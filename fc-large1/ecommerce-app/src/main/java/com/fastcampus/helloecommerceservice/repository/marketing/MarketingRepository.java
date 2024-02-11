package com.fastcampus.helloecommerceservice.repository.marketing;

import com.fastcampus.helloecommerceservice.domain.marketing.Marketing;
import com.fastcampus.helloecommerceservice.enums.AdvertiseType;
import com.fastcampus.helloecommerceservice.enums.MarketingPlacement;
import com.fastcampus.helloecommerceservice.enums.MarketingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MarketingRepository extends JpaRepository<Marketing, Long> {

    Optional<Marketing> findFirstByMarketingTypeAndMarketingPlacementAndAdvertiseType(MarketingType marketingType, MarketingPlacement marketingPlacement, AdvertiseType advertiseType);

}
