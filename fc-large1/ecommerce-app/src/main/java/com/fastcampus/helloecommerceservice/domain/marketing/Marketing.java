package com.fastcampus.helloecommerceservice.domain.marketing;

import com.fastcampus.helloecommerceservice.enums.AdvertiseType;
import com.fastcampus.helloecommerceservice.enums.MarketingPlacement;
import com.fastcampus.helloecommerceservice.enums.MarketingType;
import lombok.Data;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "marketings", schema = "ecommerce")
@Data
public class Marketing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long marketingId;
    @Enumerated(EnumType.STRING)
    @Column(name = "marketing_type")
    private MarketingType marketingType;
    @Enumerated(EnumType.STRING)
    @Column(name = "marketing_placement")
    private MarketingPlacement marketingPlacement;
    @Enumerated(EnumType.STRING)
    @Column(name = "advertise_type")
    private AdvertiseType advertiseType;
    @Column(name = "advertise_value")
    private String advertiseValue;
    @Column(name = "created_at")
    private OffsetDateTime createdAt;
    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
    @Column(name = "updated_by")
    private String updatedBy;
}
