package com.vti.config;

import com.vti.entity.SpendingLimits;
import com.vti.repository.ISpendingLimitsRepository;
import com.vti.utils.CycleUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class SpendingLimitScheduler {
    @Autowired
    private ISpendingLimitsRepository spendingLimitsRepository;

    @Scheduled(cron = "0 0 0 * * *")
    public void checkAndResetSpendingLimits() {
        List<SpendingLimits> expiredLimits = spendingLimitsRepository.findByEndDate(LocalDate.now());

        for (SpendingLimits limit : expiredLimits) {

            LocalDate newStart = limit.getEndDate().plusDays(1);
            limit.setStartDate(newStart);

            LocalDate newEnd = CycleUtil.calculateEndDate(newStart, limit.getPeriodType());
            limit.setEndDate(newEnd);

            limit.setActualSpent(0.0);

            spendingLimitsRepository.save(limit);
        }

        System.out.println("Đã cập nhật các hạn mức chi tiêu hết hạn lúc: " + LocalDateTime.now());
    }

}
