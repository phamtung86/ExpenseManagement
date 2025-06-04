package com.vti.service;

import com.vti.dto.SpendingLimits.SpendingLimitsCreateRequest;
import com.vti.dto.SpendingLimits.SpendingLimitsDTO;
import com.vti.dto.SpendingLimits.SpendingLimitsUpdateRequest;
import com.vti.entity.SpendingLimits;

import java.util.List;

public interface ISpendingLimitsService {
    List<SpendingLimitsDTO> getAll();

    SpendingLimitsDTO getById(Integer id);

    SpendingLimitsDTO create(SpendingLimitsCreateRequest request);

    SpendingLimitsDTO update(Integer id, SpendingLimitsUpdateRequest request);

    void delete(Integer id);

    SpendingLimits findByCategoriesIdAndMoneySourcesId(Integer categoriesId, Integer moneySourcesId);

    void updateActualSpent(Integer id, Double actualSpent);
}
