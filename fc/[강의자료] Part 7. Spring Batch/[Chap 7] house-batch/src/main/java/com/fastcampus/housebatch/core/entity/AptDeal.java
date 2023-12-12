package com.fastcampus.housebatch.core.entity;

import com.fastcampus.housebatch.core.dto.AptDealDto;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@DynamicUpdate
@Table(name = "apt_deal")
@EntityListeners(AuditingEntityListener.class)
public class AptDeal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long aptDealId;

    @ManyToOne
    @JoinColumn(name = "apt_id")
    private Apt apt;

    @Column(nullable = false)
    private Double exclusiveArea;

    @Column(nullable = false)
    private LocalDate dealDate;

    @Column(nullable = false)
    private Long dealAmount;

    @Column(nullable = false)
    private Integer floor;

    @Column(nullable = false)
    private boolean dealCanceled;

    @Column
    private LocalDate dealCanceledDate;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public static AptDeal of(AptDealDto dto, Apt apt) {
        AptDeal deal = new AptDeal();
        deal.setApt(apt);
        deal.setExclusiveArea(dto.getExclusiveArea());
        deal.setDealDate(dto.getDealDate());
        deal.setDealAmount(dto.getDealAmount());
        deal.setFloor(dto.getFloor());
        deal.setDealCanceled(dto.isDealCanceled());
        deal.setDealCanceledDate(dto.getDealCanceledDate());
        return deal;
    }
}
