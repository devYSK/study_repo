package com.example.demo.domain.pharmacy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PharmacyDto {

	private Long id;

	private String pharmacyName;

	private String pharmacyAddress;

	private double latitude;

	private double longitude;

}
