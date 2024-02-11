package com.fastcampus.ecommerce.admin.domain.vendor;

import com.fastcampus.ecommerce.admin.enums.VendorStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "vendors", schema = "ecommerce")
@Getter
@Setter
public class Vendor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long vendorId;
    @Column(name = "name")
    private String vendorName;
    @Enumerated(EnumType.STRING)
    private VendorStatus vendorStatus;
    @Column(name = "is_deleted")
    private boolean isDeleted;
    @Column(name = "created_at")
    private OffsetDateTime createdAt;
    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
    @Column(name = "updated_by")
    private String updatedBy;
}
