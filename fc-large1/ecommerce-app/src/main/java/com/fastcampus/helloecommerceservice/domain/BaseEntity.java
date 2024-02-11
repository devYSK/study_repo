package com.fastcampus.helloecommerceservice.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.OffsetDateTime;

@Getter
@Setter
@MappedSuperclass
public class BaseEntity {

    private final static String DEFAULT_USER = "system";

    @Column(name = "is_deleted")
    protected boolean isDeleted = false;
    @Column(name = "created_by")
    protected String createdBy = DEFAULT_USER;
    @Column(name = "created_at")
    protected OffsetDateTime createdAt = OffsetDateTime.now();
    @Column(name = "updated_by")
    private String updatedBy = DEFAULT_USER;
    @Column(name = "updated_at")
    protected OffsetDateTime updatedAt = OffsetDateTime.now();
}
