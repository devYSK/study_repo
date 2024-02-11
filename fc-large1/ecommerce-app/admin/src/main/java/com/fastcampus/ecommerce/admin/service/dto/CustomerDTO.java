package com.fastcampus.ecommerce.admin.service.dto;

import com.fastcampus.ecommerce.admin.enums.CustomerGrade;
import lombok.Data;

import java.time.OffsetDateTime;

@Data(staticConstructor = "of")
public class CustomerDTO {
    private final Long customerId;
    private final String customerName;
    private final String phoneNumber;
    private final String address;
    private final CustomerGrade customerGrade;
    private final OffsetDateTime createdAt;
    private final OffsetDateTime updatedAt;
}
