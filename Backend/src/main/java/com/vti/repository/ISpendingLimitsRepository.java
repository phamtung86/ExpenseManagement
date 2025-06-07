package com.vti.repository;

import com.vti.entity.MoneySources;
import com.vti.entity.SpendingLimits;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISpendingLimitsRepository extends JpaRepository<SpendingLimits, Integer> {

    List<SpendingLimits> findAllByUserId(int userId);

    SpendingLimits findByCategoriesIdAndMoneySourcesIdAndUserId(Integer categoriesId, Integer moneySourcesId, Integer userId);

}
