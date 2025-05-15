package com.vti.repository;

import com.vti.entity.SpendingLimits;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ISpendingLimitsRepository extends JpaRepository<SpendingLimits, Integer> {
}
