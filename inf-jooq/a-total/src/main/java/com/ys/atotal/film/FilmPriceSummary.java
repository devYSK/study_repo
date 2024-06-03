package com.ys.atotal.film;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class FilmPriceSummary {

	private Long filmId;
	private String title;
	private PriceCategory priceCategory;
	private BigDecimal rentalRate;
	private Long totalInventory;

	@Getter
	public enum PriceCategory {
		CHEAP("Cheap"),
		NORMAL("Normal"),
		EXPENSIVE("Expensive"),
		MODERATE("Moderate")
		;

		private final String code;

		PriceCategory(String code) {
			this.code = code;
		}

		public static PriceCategory findByCode (String code) {
			System.out.println("findByCode"  + code);
			for (PriceCategory value : values()) {
				if (value.code.equalsIgnoreCase(code)) {
					return value;
				}
			}
			return null;
		}
	}

}