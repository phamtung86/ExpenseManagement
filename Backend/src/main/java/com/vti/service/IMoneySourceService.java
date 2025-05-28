package com.vti.service;

import com.vti.entity.MoneySources;

public interface IMoneySourceService {

    void updateCurrentBalance(Integer id, Double amount);

    MoneySources findById(Integer id);

}
