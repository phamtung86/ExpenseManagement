package com.vti.utils;

import com.vti.entity.SpendingLimits;

import java.time.LocalDate;

public class CycleUtil {

    public static LocalDate calculateEndDate(LocalDate start, SpendingLimits.PeriodType type) {
        switch (type) {
            case DAILY:
                return start;
            case WEEKLY:
                return start.plusDays(6);
            case MONTHLY:
                return start.withDayOfMonth(start.lengthOfMonth());
            case YEARLY:
                return start.withDayOfYear(start.lengthOfYear());
            default:
                throw new IllegalArgumentException("Chu kỳ không hợp lệ");
        }
    }
}
