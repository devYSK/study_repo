package com.ys.atotal.film;

import java.math.BigDecimal;

import lombok.Getter;

@Getter
public class FilmRentalSummary {
    private Long filmId;
    private String title;
    private BigDecimal averageRentalDuration;
}
