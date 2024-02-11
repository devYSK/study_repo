package com.fastcampus.ecommerce.admin.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VendorDTO {
    private Long vendorId;
    private String vendorName;
    private String vendorPhoneNumber;
}
