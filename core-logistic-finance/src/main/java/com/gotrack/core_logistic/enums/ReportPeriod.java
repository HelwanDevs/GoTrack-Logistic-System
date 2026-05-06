package com.gotrack.core_logistic.enums;

import java.time.LocalDate;

public enum ReportPeriod {
    DAILY,
    WEEKLY,
    MONTHLY,
    YEARLY;



        public LocalDate getStartDate(LocalDate now) {
        switch (this) {
            case DAILY:
                return now;
            case WEEKLY:
                return now.minusWeeks(1);
            case MONTHLY:
                return now.minusMonths(1);
            case YEARLY:
                return now.minusYears(1);
            default:
                return now;
        }
    }

}
