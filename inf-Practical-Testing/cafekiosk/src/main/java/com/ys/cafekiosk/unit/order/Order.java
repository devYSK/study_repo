package com.ys.cafekiosk.unit.order;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.ys.cafekiosk.unit.beverage.Beverage;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Order {

	private final LocalDateTime orderDateTime;

	private final List<Beverage> beverages;


}
