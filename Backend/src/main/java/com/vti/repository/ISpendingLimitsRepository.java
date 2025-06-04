package com.vti.repository;

import com.vti.entity.SpendingLimits;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISpendingLimitsRepository extends JpaRepository<SpendingLimits, Integer> {

    SpendingLimits findByCategoriesIdAndMoneySourcesId(Integer categoriesId, Integer moneySourcesId);

}
