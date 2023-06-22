package com.fastcampus.pass.service.statistics;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ChartData {
    private List<String> labels;
    private List<Long> attendedCounts;
    private List<Long> cancelledCounts;

    public ChartData(List<String> labels, List<Long> attendedCounts, List<Long> cancelledCounts) {
        this.labels = labels;
        this.attendedCounts = attendedCounts;
        this.cancelledCounts = cancelledCounts;

    }

}
