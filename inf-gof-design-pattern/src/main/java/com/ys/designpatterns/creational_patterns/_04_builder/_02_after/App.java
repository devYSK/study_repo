package com.ys.designpatterns.creational_patterns._04_builder._02_after;

import com.ys.designpatterns.creational_patterns._04_builder._01_before.TourPlan;

import java.time.LocalDate;

public class App {

    public static void main(String[] args) {
        TourDirector director = new TourDirector(new DefaultTourBuilder());
        TourPlan tourPlan = director.cancunTrip();
        TourPlan tourPlan1 = director.longBeachTrip();

        TourPlanBuilder builder = new DefaultTourBuilder();
        TourPlan plan = builder.title("칸쿤여행")
                .nightsAndDays(2, 3)
                .startDate(LocalDate.of(2020, 12, 9))
                .whereToStay("리조트")
                .addPlan(0, "짐풀기")
                .addPlan(0, "저녁식사")
                .getPlan();

        TourPlan longBeachTrp = builder.title("롱비치")
                .startDate(LocalDate.of(2021 ,2 ,15))
                .getPlan();

    }
}
