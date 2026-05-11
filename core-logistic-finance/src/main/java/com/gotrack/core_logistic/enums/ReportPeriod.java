package com.gotrack.core_logistic.enums;

import java.time.LocalDate;

public enum ReportPeriod {
    DAILY,
    WEEKLY,
    MONTHLY,
    YEARLY;



        public LocalDate getStartDate(LocalDate now) {
        return switch (this) {
            case DAILY -> now;
            case WEEKLY -> now.minusWeeks(1);
            case MONTHLY -> now.minusMonths(1);
            case YEARLY -> now.minusYears(1);
            default -> now;
        };
    }

}
