package com.vti.service;

import com.vti.dto.MoneySourcesDTO;
import com.vti.entity.MoneySources;
import com.vti.form.MoneySourceForm;

import java.util.List;

public interface IMoneySourceService {

    void updateCurrentBalance(Integer id, Double amount);

    List<MoneySourcesDTO> getAllMoneySources(Integer userId);

    MoneySources findById(Integer id);

    MoneySources createNewMoneySource(MoneySourceForm createMoneySourceForm);

    boolean updateMoneySource(Integer id, MoneySourceForm updateMoneySourceForm);

    boolean deleteMoneySource(Integer id);

    double getTotalCurrentBalance(Integer userId);
}
