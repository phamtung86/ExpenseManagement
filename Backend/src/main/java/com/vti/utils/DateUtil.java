package com.vti.utils;

import com.vti.entity.SpendingLimits;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class DateUtil {
    public static DateRange getDateRangeByCycle(SpendingLimits.PeriodType cycle) {
        LocalDate today = LocalDate.now();

        switch (cycle) {
            case DAILY:
                return new DateRange(today, today);

            case WEEKLY:
                return new DateRange(today.with(DayOfWeek.MONDAY), today.with(DayOfWeek.SUNDAY));

            case MONTHLY:
                return new DateRange(
                        today.withDayOfMonth(1),
                        today.withDayOfMonth(today.lengthOfMonth())
                );

            case YEARLY:
                return new DateRange(
                        today.withDayOfYear(1),
                        today.withDayOfYear(today.lengthOfYear())
                );

            default:
                throw new IllegalArgumentException("Unknown cycle: " + cycle);
        }
    }
}
