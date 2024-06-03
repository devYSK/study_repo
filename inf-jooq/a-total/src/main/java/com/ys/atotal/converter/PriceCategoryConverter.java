package com.ys.atotal.converter;

import org.jooq.impl.EnumConverter;

import com.ys.atotal.film.FilmPriceSummary;

public class PriceCategoryConverter extends EnumConverter<String, FilmPriceSummary.PriceCategory>{

    // public PriceCategoryConverter() {
    //     // super(String.class, FilmPriceSummary.PriceCategory.class, FilmPriceSummary.PriceCategory::getCode);
    //     super(String.class, FilmPriceSummary.PriceCategory.class, (String code) -> {
    //         FilmPriceSummary.PriceCategory.findByCode(code);
    //     });
    // }


    public PriceCategoryConverter() {
        super(String.class, FilmPriceSummary.PriceCategory.class, FilmPriceSummary.PriceCategory::getCode);
        System.out.println("PriceCategoryConverter constructor");
    }

}
